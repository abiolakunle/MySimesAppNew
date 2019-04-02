package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
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
import com.abiolasoft.mysimesapp.Utils.ImeClasses;
import com.abiolasoft.mysimesapp.Utils.ImeExecutives;
import com.abiolasoft.mysimesapp.Utils.MultiViewHider;

public class AccountSettingsActivity extends BaseActivity {

    private Spinner settingClassSpin, executivePositionSpin;
    private TextView setPositionTv, setCodeTv;
    private EditText requestCodeEdit;
    private Switch requestExecutiveSw;
    private Button updateSetting;
    private String selectedClass;
    private int referer;
    private UserDetails userDetails;

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

        updateSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedClass = settingClassSpin.getSelectedItem().toString();
                UserRepository userRepository = new UserRepository(AccountSettingsActivity.this);
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
        });
    }

    @Override
    protected boolean useToolbar() {

        if (referer == 0) {
            return false;
        } else {
            return super.useToolbar();
        }
    }

    private void updateUI() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
