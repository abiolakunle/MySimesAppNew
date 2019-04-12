package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.SuperUserDetails;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Repositories.UserRepository;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.ImageCompressor;
import com.abiolasoft.mysimesapp.Utils.ImeClasses;
import com.abiolasoft.mysimesapp.Utils.ImeExecutives;
import com.abiolasoft.mysimesapp.Utils.MultiViewHider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsActivity extends BaseActivity {

    private Spinner settingClassSpin, executivePositionSpin;
    private TextView setPositionTv, setCodeTv;
    private EditText requestCodeEdit;
    private Switch requestExecutiveSw;
    private Button updateSetting, changeProfileImage;
    private CircleImageView profileImage;
    private String selectedClass;
    private int referer;
    private UserDetails userDetails;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        settingClassSpin = findViewById(R.id.setting_class_spinner);
        executivePositionSpin = findViewById(R.id.set_executive_list_spin);
        setPositionTv = findViewById(R.id.executive_pos_tv);
        requestExecutiveSw = findViewById(R.id.request_executive_sw);
        setCodeTv = findViewById(R.id.request_code_tv);
        requestCodeEdit = findViewById(R.id.request_code_edit);
        updateSetting = findViewById(R.id.settings_update);
        changeProfileImage = findViewById(R.id.get_profile_image_btn);
        profileImage = findViewById(R.id.profile_image_civ);

        userRepository = new UserRepository();

        requestExecutiveSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MultiViewHider multiViewHider = new MultiViewHider();
                multiViewHider.add(executivePositionSpin);
                multiViewHider.add(setPositionTv);
                multiViewHider.add(setCodeTv);
                multiViewHider.add(requestCodeEdit);

                if (isChecked) {
                    multiViewHider.unHideViews();
                } else {
                    multiViewHider.hideViews();
                }
            }
        });

        userDetails = CurrentUserRepo.getOffline();

        populateViews();

        updateSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                processAndSubmit();
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage();
            }
        });
    }

    private void populateViews() {

        Picasso.get().load(userDetails.getImage_url()).into(profileImage);

        ArrayAdapter<ImeClasses> setClassAdapt = new ArrayAdapter<ImeClasses>(this,
                android.R.layout.simple_spinner_item,
                ImeClasses.values());
        setClassAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settingClassSpin.setAdapter(setClassAdapt);

        ArrayAdapter<ImeExecutives> executivePositionAdapt = new ArrayAdapter<ImeExecutives>(this,
                android.R.layout.simple_spinner_item,
                ImeExecutives.values());
        executivePositionAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        executivePositionSpin.setAdapter(executivePositionAdapt);

        Bundle extras = getIntent().getExtras();
        referer = (extras != null) ? extras.getInt(SignInActivity.NEW_LOGIN) : 1;


        if (referer != 0) {
            settingClassSpin.setSelection(setClassAdapt.getPosition(ImeClasses.valueOf(userDetails.getLevel())));
        }

    }

    @Override
    protected boolean useToolbar() {
        if (referer == 0) {
            return false;
        } else {
            return super.useToolbar();
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
                Uri imageUri = result.getUri();
                final byte[] thumbData = ImageCompressor.compressImageForThumb(AccountSettingsActivity.this, imageUri);

                final StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();

                firebaseStorage.child(DbPaths.ProfilePicture.toString()).putFile(imageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                String imageUrl = task.getResult().getDownloadUrl().toString();
                                userDetails.setImage_url(imageUrl);

                                firebaseStorage.child(DbPaths.ProfilePictureThumb.toString()).putBytes(thumbData)
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                String thumbImageUrl = task.getResult().getDownloadUrl().toString();
                                                userDetails.setImage_thumb(thumbImageUrl);
                                                userRepository.updateUser(userDetails);
                                            }
                                        });
                            }
                        });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Toast.makeText(this, result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUI() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    private void processAndSubmit() {
        selectedClass = settingClassSpin.getSelectedItem().toString();
        SuperUserDetails superUserDetails;

        if (requestExecutiveSw.isActivated()) {

            superUserDetails = (SuperUserDetails) userDetails;
            superUserDetails.setAccess_code("50000");
            userDetails = superUserDetails;
            Toast.makeText(AccountSettingsActivity.this, "Cast Done", Toast.LENGTH_SHORT).show();
        }

        if (selectedClass == null) {
            Toast.makeText(AccountSettingsActivity.this, "Please select class", Toast.LENGTH_SHORT).show();
        } else {
            userDetails.setLevel(ImeClasses.get(selectedClass).name());

            userRepository.updateUser(userDetails);
            CurrentUserRepo.updateCurrentUser(userDetails);
            updateUI();
        }
    }

    private void cropImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMaxCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(this);
    }
}
