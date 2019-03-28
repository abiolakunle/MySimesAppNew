package com.abiolasoft.mysimesapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.EBook;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UploadToLibraryActivity extends BaseActivity {

    private Button selectFileBtn, startUploadBtn;
    private TextView displayFileName;
    private String fileName;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_library);

        selectFileBtn = findViewById(R.id.library_select_file_btn);
        startUploadBtn = findViewById(R.id.start_upload_btn);
        displayFileName = findViewById(R.id.display_filename);

        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadToLibraryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileSelect();
                } else {
                    ActivityCompat.requestPermissions(UploadToLibraryActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        startUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri != null) {
                    uploadFile(fileUri);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFileSelect();
        } else {
            Toast.makeText(this, "Please provide permissions", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            fileName = getFileName(data.getData());
            fileUri = data.getData();
            displayFileName.setText("You've selected " + fileName.toUpperCase());
        } else {
            Toast.makeText(this, "No file selected, Select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
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

    private void openFileSelect() {
        /*Intent selectFileIntent = new Intent();
        selectFileIntent.setType("application/pdf");
        selectFileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(selectFileIntent, 86);*/

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"application/pdf", "application/doc"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, 86);
    }

    private void uploadFile(Uri uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        final UserDetails userDetail = CurrentUserRepo.getOffline();
        final EBook book = new EBook();
        final String DB_PATH = "ELibrary";

        storageReference.child(DB_PATH).child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        book.setUploader_id(userDetail.getId());
                        book.addTag(userDetail.getDisplayName());
                        book.setBook_title(fileName);
                        book.setUrl(taskSnapshot.getDownloadUrl().toString());
                        book.setFile_size(Long.valueOf(taskSnapshot.getTotalByteCount() / 1024));
                        book.setUploaded_by(userDetail.getDisplayName());
                        book.setTime_uploaded(System.currentTimeMillis() + "");
                        book.setFile_type(fileName.substring(fileName.lastIndexOf(".")));

                        FirebaseFirestore bookDb = FirebaseFirestore.getInstance();

                        bookDb.collection(DbPaths.ELibrary.toString()).add(book).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(UploadToLibraryActivity.this, "File details written to Db", Toast.LENGTH_SHORT).show();
                                userDetail.addToDocuments(documentReference.getId());
                                CurrentUserRepo.updateCurrentUser(userDetail);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadToLibraryActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


                        Intent eLibraryIntent = new Intent(UploadToLibraryActivity.this, ELibraryActivity.class);
                        startActivity(eLibraryIntent);
                        UploadToLibraryActivity.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadToLibraryActivity.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                Toast.makeText(UploadToLibraryActivity.this, String.valueOf(currentProgress), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
