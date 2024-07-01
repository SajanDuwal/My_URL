package com.sajiman.mychat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.sajiman.mychat.R;
import com.sajiman.mychat.utility.AppText;
import com.sajiman.mychat.utility.PrefManager;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PrefManager pref = new PrefManager(SplashActivity.this, AppText.PREF_USER);
        if (pref.getKeyIsLogin()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            initUi();
        }
    }

    private void initUi() {
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                finish();
                break;

            case R.id.btnLogin:
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}