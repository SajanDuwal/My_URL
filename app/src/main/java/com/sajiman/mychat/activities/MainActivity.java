package com.sajiman.mychat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sajiman.mychat.R;
import com.sajiman.mychat.utility.AppText;
import com.sajiman.mychat.utility.PrefManager;

public class MainActivity extends AppCompatActivity {
    private PrefManager pref;
    private Toolbar mTbMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        pref = new PrefManager(MainActivity.this, AppText.PREF_USER);
        setUpToolbar();

    }

    private void setUpToolbar() {
        mTbMain = findViewById(R.id.tbMain);
        setSupportActionBar(mTbMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(MainActivity.this)
                .inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                pref.setKeyIsLogin(false);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}