package com.sajiman.mychat.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private SharedPreferences pref;

    public PrefManager(Context context, String prefName) {
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public void setKeyIsLogin(boolean value) {
        pref.edit().putBoolean(AppText.KEY_IS_LOGIN, value).apply();
    }

    public boolean getKeyIsLogin() {
        return pref.getBoolean(AppText.KEY_IS_LOGIN, false);
    }

    public void setKeyRememberMe(boolean value) {
        pref.edit().putBoolean(AppText.KEY_REMEMBER_ME, value).apply();
    }

    public boolean getKeyRememberMe() {
        return pref.getBoolean(AppText.KEY_REMEMBER_ME, false);
    }

    public void setKeyUsername(String username) {
        pref.edit().putString(AppText.KEY_USERNAME, username).apply();
    }

    public void setKeyPassword(String password) {
        pref.edit().putString(AppText.KEY_PASSWORD, password).apply();
    }

    public String getKeyUsername() {
        return pref.getString(AppText.KEY_USERNAME, "");
    }

    public String getKeyPassword() {
        return pref.getString(AppText.KEY_PASSWORD, "");
    }

    public void setClearRememberMeValue() {
        pref.edit().remove(AppText.KEY_USERNAME).apply();
        pref.edit().remove(AppText.KEY_PASSWORD).apply();
    }


}
