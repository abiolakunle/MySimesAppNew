package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.abiolasoft.mysimesapp.Adapters.ELibraryAdapter;
import com.abiolasoft.mysimesapp.Models.Course;
import com.abiolasoft.mysimesapp.Models.EBook;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.ImeClasses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class EBookViewerActivity extends BaseActivity {

    private Button eBookDownloadBtn;
    private TextView eBookTitle, eBookUploader, eBookSize, eBookType, eBookModifyTags;
    private ListView eBookTagsLv;
    private String eBookId;
    private EBook eBook;
    private ArrayAdapter<String> tagsAdapter;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<MultiSelectModel> allTagsList;
    private ArrayList<String> allTagListStrings;
    private List<String> preSelectedTags;
    private ArrayList<Integer> selectedTagsIds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_viewer);

        eBookDownloadBtn = findViewById(R.id.ebook_view_download_btn);
        eBookTitle = findViewById(R.id.ebook_viewer_book_title);
        eBookUploader = findViewById(R.id.ebook_viewer_uploader);
        eBookSize = findViewById(R.id.ebook_viewer_size);
        eBookType = findViewById(R.id.ebook_view_filetype);
        eBookModifyTags = findViewById(R.id.ebook_viewer_edit_tags);
        eBookTagsLv = findViewById(R.id.ebook_viewer_tags_lv);

        firebaseFirestore = FirebaseFirestore.getInstance();


        allTagsList = new ArrayList<MultiSelectModel>();
        allTagListStrings = new ArrayList<String>();
        preSelectedTags = new ArrayList<String>();
        selectedTagsIds = new ArrayList<Integer>();

        tagsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, preSelectedTags);

        eBookTagsLv.setAdapter(tagsAdapter);

        eBookId = getIntent().getStringExtra(ELibraryAdapter.BOOK_ID);

        loadBook();
        loadTags();


        eBookDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW); // denotes that we are going to view something
                intent.setData(Uri.parse(eBook.getUrl()));
                startActivity(intent);
            }
        });

    }

    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }

    private void loadBook() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection(DbPaths.ELibrary.toString()).document(eBookId);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                eBook = documentSnapshot.toObject(EBook.class);
                eBookTitle.setText(eBook.getBook_title());
                eBookUploader.setText("Uploaded by: " + eBook.getUploaded_by());
                eBookSize.setText("File size: " + eBook.getFile_size() + "KB");
                eBookType.setText("File type: " + eBook.getFile_type());

                preSelectedTags.clear();
                for (String tag : eBook.getBook_tags()) {
                    preSelectedTags.add(tag);
                    tagsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void createTagMultiSelect() {

        //MultiSelectModel
        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Add tags to book") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(20) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(selectedTagsIds) //List of ids that you need to be selected
                .multiSelectList(allTagsList) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {

                        eBookModifyTags.setText(selectedNames.size() + ((selectedNames.size() > 1) ? " tags added, tap to modify" : " tag added, tap to modify"));


                        eBook.getBook_tags().clear();
                        for (String tag : selectedNames) {
                            eBook.addTag(tag);
                        }

                        firebaseFirestore.collection(DbPaths.ELibrary.toString()).document(eBookId).set(eBook).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EBookViewerActivity.this, "EBook tags updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        //Log.d(TAG,"Dialog cancelled");
                    }
                });

        eBookModifyTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });
    }

    private void createSelectedTagsIds() {
        for (int i = 0; i < allTagListStrings.size(); i++) {
            for (int j = 0; j < preSelectedTags.size(); j++) {
                if (allTagListStrings.get(i).equals(preSelectedTags.get(j))) {
                    selectedTagsIds.add(i);
                    Toast.makeText(this, preSelectedTags.get(j), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void loadTags() {
        CollectionReference dbRef = firebaseFirestore.getInstance().collection(DbPaths.Courses.toString());
        dbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                int position = 0;
                allTagsList.clear();
                for (DocumentSnapshot result : documentSnapshots) {
                    allTagsList.add(new MultiSelectModel(position++, result.toObject(Course.class).getCourseName()));
                    allTagListStrings.add(result.toObject(Course.class).getCourseName());
                    //Toast.makeText(EBookViewerActivity.this, result.toObject(Course.class).getCourseName(), Toast.LENGTH_SHORT).show();
                    //libraryMultiUploadAdapter.notifyDataSetChanged();
                }
                for (ImeClasses classTag : ImeClasses.values()) {
                    allTagsList.add(new MultiSelectModel(position++, classTag.name()));
                    allTagListStrings.add(classTag.name());
                }

                createSelectedTagsIds();
                createTagMultiSelect();

            }
        });

    }


}
