package com.example.gityim.wintereaxmination;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.example.gityim.wintereaxmination.Adapter.InfoListAdapter;
import com.example.gityim.wintereaxmination.bean.Item;
import com.example.gityim.wintereaxmination.http.HttpCallbackListener;
import com.example.gityim.wintereaxmination.http.HttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FireNewsActivity extends AppCompatActivity implements RecyclerClickListener{
    ArrayList<Item> fireDatas;
    RecyclerView fireList;
    InfoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_news);
        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.fire_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("近日热闻");
        initData();
        initView();
    }

    private void initView() {
        fireList = findViewById(R.id.fire_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        fireList.setLayoutManager(mLinearLayoutManager);
        mAdapter= new InfoListAdapter(fireDatas,this);
        fireList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        fireDatas =new ArrayList<>();
        String url="https://news-at.zhihu.com/api/4/news/latest";
        HttpConnect.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("QWQ", "onFinish:shortComments ");
                ParseJSONForFireStories(response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("错误", "onError: ");
                e.printStackTrace();
            }
        });
    }

    private void ParseJSONForFireStories(String JSONData) {
        try {
            JSONObject jsonObject =new JSONObject(JSONData);
            JSONArray response =jsonObject.optJSONArray("top_stories");
            for (int i = 0; i <response.length() ; i++) {
                JSONObject item = response.optJSONObject(i);
                Item listItem = new Item();
                //创建list每一个对象，并封装
                listItem.setTitle(item.optString("title"));
                listItem.setPicurl(item.getString("image"));
                listItem.setId(item.optString("id"));
                fireDatas.add(listItem);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();//通知适配器刷新数据

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Item item = fireDatas.get(position);
        String id = item.getId();
        String title = item.getTitle();
        String imgUrl = item.getPicurl();
        Intent intent = new Intent(FireNewsActivity.this, NewsInfoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(intent);
    }
}
