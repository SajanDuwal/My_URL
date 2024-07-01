package com.sajiman.mychat.responseController;

public interface ResponseListener {
    void onStart();

    void onFinish(String response);
}
