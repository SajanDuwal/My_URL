package com.sajiman.mychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sajiman.mychat.R;
import com.sajiman.mychat.controller.GetPostResponseController;
import com.sajiman.mychat.controller.OnResponseListener;
import com.sajiman.mychat.dataModels.UserDto;
import com.sajiman.mychat.utility.AppLog;
import com.sajiman.mychat.utility.AppText;
import com.sajiman.mychat.utility.MiscellaneousUtils;
import com.sajiman.mychat.utility.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mTbLogin;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private CheckBox mCbRememberMe;
    private TextView mTvLoginFailed;
    private ProgressDialog progressDialog;
    private PrefManager prefManager;

    private boolean isRememberMeChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpToolbar();
        prefManager = new PrefManager(LoginActivity.this, AppText.PREF_USER);
        initUi();
        setRememberMeValue();
        setUpProgressDialog();
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Login....");
        progressDialog.setCancelable(false);
    }

    private void setRememberMeValue() {
        if (prefManager.getKeyRememberMe()) {
            mCbRememberMe.setChecked(true);
            mEtUsername.setText(prefManager.getKeyUsername());
            mEtPassword.setText(prefManager.getKeyPassword());
        } else {
            mCbRememberMe.setChecked(false);
        }
    }

    private void initUi() {
        mEtUsername = findViewById(R.id.etUsername);
        mEtPassword = findViewById(R.id.etPassword);
        mCbRememberMe = findViewById(R.id.cbRememberMe);
        mTvLoginFailed = findViewById(R.id.tvLoginFailed);

        mCbRememberMe.setOnCheckedChangeListener(mOnRememberMeChecked);
        findViewById(R.id.btnLogIn).setOnClickListener(this);
    }

    private void setUpToolbar() {
        mTbLogin = findViewById(R.id.tbLogin);
        setSupportActionBar(mTbLogin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Login");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogIn:
                if (isFieldValid()) {
                    String username = mEtUsername.getText().toString();
                    String password = mEtPassword.getText().toString();

                    UserDto userDto = new UserDto();
                    userDto.setUsername(username);
                    userDto.setPassword(password);

                    StringBuilder param = MiscellaneousUtils.getLoginParam(userDto);

                    GetPostResponseController getPostResponseController =
                            new GetPostResponseController(AppText.URL_LOGIN, mOnResponseListener);
                    getPostResponseController.execute(param.toString());
                }
                break;
        }
    }

    private boolean isFieldValid() {
        if (TextUtils.isEmpty(mEtUsername.getText().toString())) {
            mEtUsername.setError("forget your username?");
            mEtUsername.requestFocus();
            return false;
        } else {
            mEtUsername.clearFocus();
            mEtUsername.setError(null);
        }
        if (TextUtils.isEmpty(mEtPassword.getText().toString())) {
            mEtPassword.setError("forget your password?");
            mEtPassword.requestFocus();
            return false;
        } else {
            mEtPassword.clearFocus();
            mEtPassword.setError(null);
        }
        return true;
    }

    CompoundButton.OnCheckedChangeListener mOnRememberMeChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                isRememberMeChecked = true;
            } else {
                isRememberMeChecked = false;
                prefManager.setKeyRememberMe(false);
                prefManager.setClearRememberMeValue();
            }
        }
    };

    OnResponseListener mOnResponseListener = new OnResponseListener() {
        @Override
        public void onStarted(String url) {
            progressDialog.show();
            showLog("Url connecting --> " + url);
        }

        @Override
        public void onFinished(String response) {
            progressDialog.dismiss();
            try {
                showLog("Response is -->" + response);
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean success = jsonObjectResponse.getBoolean("success");
                if (success) {
                    if (jsonObjectResponse.has("message")) {
                        prefManager.setKeyIsLogin(true);
                        if (isRememberMeChecked) {
                            prefManager.setKeyRememberMe(true);
                            prefManager.setKeyUsername(mEtUsername.getText().toString());
                            prefManager.setKeyPassword(mEtPassword.getText().toString());
                        }
                        String message = jsonObjectResponse.getString("message");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    if (jsonObjectResponse.has("message")) {
                        String message = jsonObjectResponse.getString("message");
                        mTvLoginFailed.setVisibility(View.VISIBLE);
                        mTvLoginFailed.setText(message);
                    }
                }
            } catch (
                    JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed() {
            progressDialog.dismiss();
            showLog("Failed to register--> response is null");
            Toast.makeText(LoginActivity.this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    };

    private void showLog(String message) {
        AppLog.showAppLog(LoginActivity.class.getSimpleName(), message);
    }

}
