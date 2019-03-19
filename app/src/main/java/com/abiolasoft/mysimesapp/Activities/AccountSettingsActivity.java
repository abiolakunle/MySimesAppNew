package com.abiolasoft.mysimesapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Repositories.UserRepository;
import com.abiolasoft.mysimesapp.Utils.ImeClasses;

public class AccountSettingsActivity extends BaseActivity {

    private Spinner settingClassSpin;
    private String selectedClass;
    private Button updateSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        settingClassSpin = findViewById(R.id.setting_class_spinner);
        updateSetting = findViewById(R.id.settings_update);


        ArrayAdapter<ImeClasses> setClassAdapt = new ArrayAdapter<ImeClasses>(this,
                android.R.layout.simple_list_item_1,
                ImeClasses.values());

        settingClassSpin.setAdapter(setClassAdapt);



        updateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedClass = settingClassSpin.getSelectedItem().toString();
                UserRepository userRepository = new UserRepository(AccountSettingsActivity.this);
                UserDetails userDetails = CurrentUserRepo.getOffline();
                userDetails.setLevel(ImeClasses.get(selectedClass).name());

                userRepository.updateUser(userDetails);

                CurrentUserRepo.updateCurrentUser(userDetails);
                Toast.makeText(AccountSettingsActivity.this, ImeClasses.get(selectedClass).name(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }
}
