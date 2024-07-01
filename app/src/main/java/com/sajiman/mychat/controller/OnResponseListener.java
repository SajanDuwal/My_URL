package com.sajiman.mychat.controller;

public interface OnResponseListener {
    void onStarted(String url);

    void onFinished(String response);

    void onFailed();
}
