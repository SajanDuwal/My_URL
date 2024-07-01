package com.sajiman.mychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.sajiman.mychat.R;
import com.sajiman.mychat.controller.GetPostResponseController;
import com.sajiman.mychat.controller.OnResponseListener;
import com.sajiman.mychat.dataModels.UserDto;
import com.sajiman.mychat.utility.AppLog;
import com.sajiman.mychat.utility.AppText;
import com.sajiman.mychat.utility.MiscellaneousUtils;
import com.sajiman.mychat.utility.SignUpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mTbSignUp;
    private EditText mEtFullName;
    private EditText mEtUsername;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private RadioGroup mRgGender;
    private Spinner mSpinnerDay;
    private Spinner mSpinnerMonth;
    private Spinner mSpinnerYear;
    private String day;
    private String month;
    private String year;
    private ProgressDialog progressDialog;

    private ArrayAdapter dayArrayAdapter;
    private ArrayAdapter monthArrayAdapter;
    private ArrayAdapter yearAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpToolbar();
        setUpSpinnerAdapter();
        initUi();
        setUpProgressDialog();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if (isFieldValid()) {
                    String fullName = mEtFullName.getText().toString();
                    String username = mEtUsername.getText().toString();
                    String email = mEtEmail.getText().toString();
                    String password = mEtPassword.getText().toString();
                    int radiobuttonChecked = mRgGender.getCheckedRadioButtonId();
                    String gender = SignUpUtils.getGender(radiobuttonChecked);

                    UserDto userDto = new UserDto();
                    userDto.setFullName(fullName);
                    userDto.setUsername(username);
                    userDto.setEmail(email);
                    userDto.setPassword(password);
                    userDto.setGender(gender);
                    userDto.setDay(day);
                    userDto.setMonth(month);
                    userDto.setYear(year);

                    showLog("user info " + userDto.toString());
                    StringBuilder paramsBuilder = MiscellaneousUtils.getParam(userDto);
                    showLog(paramsBuilder.toString());

                    GetPostResponseController getPostResponseController =
                            new GetPostResponseController(AppText.URL_REGISTER, mOnResponseListener);
                    getPostResponseController.execute(paramsBuilder.toString());
                }
                break;
        }
    }

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
                showLog("On Finished response is " + response);
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    if (jsonObject.has("message")) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    if (jsonObject.has("message")) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed() {
            progressDialog.dismiss();
            showLog("Failed to register--> response is null");
            Toast.makeText(SignUpActivity.this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    };

    private void setUpToolbar() {
        mTbSignUp = findViewById(R.id.tbSignUp);
        setSupportActionBar(mTbSignUp);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Create new account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initUi() {
        mEtFullName = findViewById(R.id.etName);
        mEtUsername = findViewById(R.id.etUsername);
        mEtEmail = findViewById(R.id.etEmail);
        mEtPassword = findViewById(R.id.etPassword);
        mEtConfirmPassword = findViewById(R.id.etConfirmPassword);
        mRgGender = findViewById(R.id.radioGroup);
        mSpinnerDay = findViewById(R.id.spinnerDay);
        mSpinnerMonth = findViewById(R.id.spinnerMonth);
        mSpinnerYear = findViewById(R.id.spinnerYear);
        mSpinnerDay.setAdapter(dayArrayAdapter);
        mSpinnerMonth.setAdapter(monthArrayAdapter);
        mSpinnerYear.setAdapter(yearAdapter);

        mSpinnerDay.setOnItemSelectedListener(mOnSpinnerDaySelected);
        mSpinnerMonth.setOnItemSelectedListener(mOnSpinnerMothSelected);
        mSpinnerYear.setOnItemSelectedListener(mOnSpinnerYearSelected);

        findViewById(R.id.btnRegister).setOnClickListener(this);
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Registering....");
        progressDialog.setCancelable(false);
    }

    private void setUpSpinnerAdapter() {
        dayArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, SignUpUtils.getDays());
        monthArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, SignUpUtils.getMonths());
        yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, SignUpUtils.getYear());
    }

    public void showLog(String message) {
        AppLog.showAppLog(SignUpActivity.class.getSimpleName(), message);
    }

    private boolean isFieldValid() {
        if (TextUtils.isEmpty(mEtFullName.getText().toString())) {
            mEtFullName.setError("What's your name?");
            mEtFullName.requestFocus();
            return false;
        } else {
            mEtFullName.clearFocus();
            mEtFullName.setError(null);
        }
        if (TextUtils.isEmpty(mEtUsername.getText().toString())) {
            mEtUsername.setError("You'll need it while login your account");
            mEtUsername.requestFocus();
            return false;
        } else {
            mEtUsername.clearFocus();
            mEtUsername.setError(null);
        }

        if ((!Patterns.EMAIL_ADDRESS.matcher(mEtEmail.getText().toString()).matches())) {
            mEtEmail.setError("invalid email");
            mEtEmail.requestFocus();
            return false;
        } else {
            mEtEmail.clearFocus();
            mEtEmail.setError(null);
        }

        if (TextUtils.isEmpty(mEtPassword.getText().toString())) {
            mEtPassword.requestFocus();
            mEtPassword.setError("password can't be empty");
            return false;
        } else {
            mEtPassword.clearFocus();
            mEtPassword.setError(null);
        }
        if ((mEtPassword.getText().toString()).length() < 7) {
            mEtPassword.requestFocus();
            mEtPassword.setError("password must contain more than 6 characters");
            return false;
        } else {
            mEtPassword.clearFocus();
            mEtPassword.setError(null);
        }

        if (!(mEtConfirmPassword.getText().toString()).equals(mEtPassword.getText().toString())) {
            mEtConfirmPassword.setError("password doesn't match");
            mEtConfirmPassword.requestFocus();
            return false;
        } else {
            mEtConfirmPassword.clearFocus();
            mEtConfirmPassword.setError(null);
        }

        if (day.equals("Day") || month.equals("Month") || year.equals("Year")) {
            Toast.makeText(SignUpActivity.this, "have you forget your birthday!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(SignUpActivity.this, SplashActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Spinner.OnItemSelectedListener mOnSpinnerDaySelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SignUpActivity.this.day = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Spinner.OnItemSelectedListener mOnSpinnerMothSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SignUpActivity.this.month = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Spinner.OnItemSelectedListener mOnSpinnerYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SignUpActivity.this.year = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
