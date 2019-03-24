package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectModel;
import com.abiolasoft.mysimesapp.Adapters.LibraryMultiUploadAdapter;
import com.abiolasoft.mysimesapp.Models.Course;
import com.abiolasoft.mysimesapp.Models.EBook;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.ImeClasses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadMultipleToLibraryActivity extends BaseActivity {

    private static int RESULT_LOAD_FILE = 1;
    private Button selectFileBtn, startUploadBtn;
    private RecyclerView recyclerView;
    private List<Uri> fileUri = new ArrayList<>();
    private List<String> fileNameDatabase = new ArrayList<>();

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<Double> progressList;
    private List<Integer> progVisibility;
    private ArrayList<MultiSelectModel> courses;
    private FragmentManager fragmentManager;

    private LibraryMultiUploadAdapter libraryMultiUploadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_multiple_to_library);

        selectFileBtn = findViewById(R.id.upload_multi_select_file_btn);
        startUploadBtn = findViewById(R.id.lib_multi_start_upload_btn);
        recyclerView = findViewById(R.id.lib_upload_multi_recycler);


        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        progressList = new ArrayList<>();
        progVisibility = new ArrayList<>();
        courses = new ArrayList<MultiSelectModel>();

        loadTags();

        fragmentManager = getSupportFragmentManager();
        libraryMultiUploadAdapter = new LibraryMultiUploadAdapter(fragmentManager, fileNameList, progressList, courses);
        recyclerView.setAdapter(libraryMultiUploadAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select Picture"), RESULT_LOAD_FILE);
            }
        });

        startUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload Button
                if (LibraryMultiUploadAdapter.getEBookTagList().size() < fileNameList.size()) {
                    Toast.makeText(UploadMultipleToLibraryActivity.this, "Please tag books to help organise the library", Toast.LENGTH_SHORT).show();
                } else {
                    uploadToDb();
                    startUploadBtn.setEnabled(false);
                    startUploadBtn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

    }


    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemSelected; i++) {

                    fileUri.add(data.getClipData().getItemAt(i).getUri());
                    String fileName = getFileName(fileUri.get(i));
                    fileNameDatabase.add(fileName.substring(0, fileName.lastIndexOf('.')));


                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    progressList.add(i, 0.0);
                    progVisibility.add(i, View.INVISIBLE);
                    libraryMultiUploadAdapter.notifyDataSetChanged();

                }
                //Toast.makeText(this, "You selected multiple files", Toast.LENGTH_LONG).show();
            } else if (data.getData() != null) {

                Toast.makeText(this, "You selected a single file", Toast.LENGTH_LONG).show();
            }

        }
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void uploadToDb() {

        final CollectionReference dbRef = FirebaseFirestore.getInstance().collection(DbPaths.ELibrary.toString());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(DbPaths.ELibrary.toString());

        final Map<Integer, ArrayList> eBookTags = LibraryMultiUploadAdapter.getEBookTagList();

        for (int i = 0; i < fileNameList.size(); i++) {
            final int position = i;
            storageRef.child(fileNameDatabase.get(i)).putFile(fileUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    EBook uploadedBook = new EBook();
                    uploadedBook.setBook_title(fileNameList.get(position));
                    uploadedBook.setBook_id(String.valueOf(taskSnapshot.hashCode()));
                    uploadedBook.setBook_url(taskSnapshot.getDownloadUrl().toString());
                    uploadedBook.setFile_size(taskSnapshot.getTotalByteCount() / (1024));
                    uploadedBook.setFile_type(fileNameList.get(position).substring(fileNameList.get(position).lastIndexOf(".")));
                    uploadedBook.setUploaded_by(CurrentUserRepo.getOffline().getDisplayName());
                    uploadedBook.setUploader_id(CurrentUserRepo.getOffline().getId());
                    uploadedBook.setBook_tags(eBookTags.get(position));


                    dbRef.document(String.valueOf(taskSnapshot.hashCode())).set(uploadedBook).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UploadMultipleToLibraryActivity.this, "Details written to Db", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressList.set(position, progress);
                    libraryMultiUploadAdapter.notifyDataSetChanged();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    updateUI();
                }
            });

        }

    }

    private void loadTags() {
        CollectionReference dbRef = FirebaseFirestore.getInstance().collection(DbPaths.Courses.toString());
        dbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                int position = 0;
                courses.clear();
                for (DocumentSnapshot result : documentSnapshots) {
                    courses.add(new MultiSelectModel(position++, result.toObject(Course.class).getCourseName()));
                    Toast.makeText(UploadMultipleToLibraryActivity.this, result.toObject(Course.class).getCourseName(), Toast.LENGTH_SHORT).show();
                    libraryMultiUploadAdapter.notifyDataSetChanged();
                }
                for (ImeClasses classTag : ImeClasses.values()) {
                    courses.add(new MultiSelectModel(position++, classTag.name()));
                }
            }
        });

    }

    private void updateUI() {
        Intent libraryIntent = new Intent(this, ELibraryActivity.class);
        startActivity(libraryIntent);
    }
}
