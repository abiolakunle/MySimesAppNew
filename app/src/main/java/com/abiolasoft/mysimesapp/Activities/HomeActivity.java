package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.HomeMessageAdapter;
import com.abiolasoft.mysimesapp.Models.ImageMessage;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.SeedDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.zelory.compressor.Compressor;

public class HomeActivity extends BaseActivity {

    FirebaseFirestore firebaseFirestore;
    HomeMessageAdapter homeMessageAdapter;
    private FirebaseAuth auth;
    private Button sendBtn, addImageBtn;
    private TextView messageEt;
    private ImageView messageImageView;
    private RecyclerView recyclerView;
    private ImageMessage messageModel;
    private String imageName;
    private Uri imageUri;
    private List<ImageMessage> messagesList;
    private List<String> messageIdList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sendBtn = findViewById(R.id.home_message_send_btn);
        addImageBtn = findViewById(R.id.home_mesage_add_image_btn);
        messageEt = findViewById(R.id.home_message_text);
        messageImageView = findViewById(R.id.home_msg_iv);
        recyclerView = findViewById(R.id.home_recycler_view);

        messagesList = new ArrayList<>();
        messageIdList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        homeMessageAdapter = new HomeMessageAdapter(messagesList, messageIdList);
        recyclerView.setAdapter(homeMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMessagesFromDb();

        messageModel = new ImageMessage();


        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        SeedDatabase.populateCourses(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        Intent redirectIntent;
        if (auth.getCurrentUser() == null) {
            redirectIntent = new Intent(this, SignInActivity.class);
            startActivity(redirectIntent);
            finish();
        }
        if (CurrentUserRepo.getOffline().getLevel() == null) {
            redirectIntent = new Intent(this, AccountSettingsActivity.class);
            redirectIntent.putExtra(SignInActivity.NEW_LOGIN, 0);
            startActivity(redirectIntent);
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cropImage();
        } else {
            Toast.makeText(this, "Please provide permissions", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                messageImageView.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Toast.makeText(this, result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage() {

        UserDetails currUser = CurrentUserRepo.getOffline();

        if (!messageEt.getText().equals("")) {
            messageModel.setMessage_text(messageEt.getText().toString());
            messageEt.setText("");
            messageModel.setSender_name(currUser.getDisplayName());
            messageModel.setSender_id(currUser.getId());
            messageModel.setSender_image(currUser.getImage_thumb());
            messageModel.setSender_image_url(CurrentUserRepo.getOffline().getImage_thumb());
            messageModel.setMessage_time(String.valueOf(System.currentTimeMillis()));

            if (!(imageUri == null)) {

                final String randomName = UUID.randomUUID().toString();
                final StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();

                firebaseStorage.child(DbPaths.MessageImages.toString()).child(randomName + ".jpg")
                        .putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            String downloadUrl = task.getResult().getDownloadUrl().toString();
                            messageModel.setImage_url(downloadUrl);

                            byte[] thumbData = compressImageForThumb();
                            firebaseStorage.child(DbPaths.MessageImageThumbs.toString())
                                    .child(randomName + ".jpg").putBytes(thumbData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    String downloadThumbUri = taskSnapshot.getDownloadUrl().toString();

                                    messageModel.setImage_thumb(downloadThumbUri);

                                }
                            });

                        }
                    }
                });

            }

            firebaseFirestore.collection(DbPaths.HomeMessages.toString()).add(messageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(HomeActivity.this, "Post was added", Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

    private byte[] compressImageForThumb() {

        Bitmap compressedImageFile = null;
        File newImageFile = new File(imageUri.getPath());
        try {
            compressedImageFile = new Compressor(HomeActivity.this)
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(5)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, boas);

        return boas.toByteArray();

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

    private void cropImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                //.setMaxCropResultSize(512,512)
                //.setAspectRatio(1,1)
                .start(this);
    }

    private void loadMessagesFromDb() {

        firebaseFirestore.collection(DbPaths.HomeMessages.toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                messagesList.clear();
                messageIdList.clear();
                for (DocumentSnapshot results : documentSnapshots) {
                    ImageMessage message = results.toObject(ImageMessage.class);
                    messageIdList.add(results.getId());
                    messagesList.add(message);

                    homeMessageAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}
