package com.example.gityim.wintereaxmination.http;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
