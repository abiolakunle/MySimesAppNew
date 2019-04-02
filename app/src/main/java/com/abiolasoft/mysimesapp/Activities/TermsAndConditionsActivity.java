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

public class TermsAndConditionsActivity extends AppCompatActivity {

    private WebView termsConditionWv;
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_terms_and_conditions);

        continueBtn = findViewById(R.id.terms_agree_btn);

        termsConditionWv = findViewById(R.id.terms_and_condition_wv);
        termsConditionWv.getSettings().setJavaScriptEnabled(true);
        termsConditionWv.loadUrl("file:///android_asset/terms_and_condition.html"); //new.html is html file name.


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privacyIntent = new Intent(TermsAndConditionsActivity.this, PrivacyPolicyActivity.class);
                startActivity(privacyIntent);
                finish();
            }
        });

    }
}
