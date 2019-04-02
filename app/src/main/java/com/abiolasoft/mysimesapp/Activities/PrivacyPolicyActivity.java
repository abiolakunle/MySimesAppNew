package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.abiolasoft.mysimesapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private Button continueBtn;
    private WebView privacyWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_privacy_policy);

        continueBtn = findViewById(R.id.privacy_agree_btn);

        privacyWv = findViewById(R.id.privacy_policy_wv);
        privacyWv.getSettings().setJavaScriptEnabled(true);
        privacyWv.loadUrl("file:///android_asset/privacy_policy.html"); //new.html is html file name.

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(PrivacyPolicyActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
