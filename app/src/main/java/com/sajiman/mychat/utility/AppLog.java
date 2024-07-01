package com.sajiman.mychat.utility;

import android.util.Log;

import com.sajiman.mychat.BuildConfig;

public class AppLog {

    public static void showAppLog(String classTag, String message) {
        if (!BuildConfig.BUILD_TYPE.equals("release")) {
            Log.e("MyChat", classTag + " ::: " + message);
        }
    }
}
