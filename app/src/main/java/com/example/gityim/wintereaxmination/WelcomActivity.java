package com.example.gityim.wintereaxmination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.logging.Handler;

public class WelcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(WelcomActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
