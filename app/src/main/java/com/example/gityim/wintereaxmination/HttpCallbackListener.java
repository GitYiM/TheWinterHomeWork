package com.example.gityim.wintereaxmination;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
