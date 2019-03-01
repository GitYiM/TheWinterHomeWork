package com.example.gityim.wintereaxmination;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class SetOptionActivity extends AppCompatActivity {

    private int status;
    private String latest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_option);
        TextView settTextView=findViewById(R.id.check_update);
        android.support.v7.widget.Toolbar toolbar;
        toolbar=findViewById(R.id.setHead_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("设置选项");
        settTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpConnect.sendHttpRequest("https://news-at.zhihu.com/api/4/version/android/2.6.0", new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {

                        ParseStatus(response);
                        Log.d("QWQ", "onFinish: "+status);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(status==0){
                                    Toast.makeText(SetOptionActivity.this,"已是最新版本2.6.0",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SetOptionActivity.this,"请最新版本2.6.0",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("错误", "onError: ");
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private void ParseStatus(String response) {
        try {
            JSONObject jsonObject =new JSONObject(response);
            status=jsonObject.optInt("status");
            latest=jsonObject.optString("latest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
