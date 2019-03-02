package com.example.gityim.wintereaxmination;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.gityim.wintereaxmination.Adapter.CommentAdapter;
import com.example.gityim.wintereaxmination.bean.Comment;
import com.example.gityim.wintereaxmination.http.HttpCallbackListener;
import com.example.gityim.wintereaxmination.http.HttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShortCommentActivity extends AppCompatActivity {
    private String id;
    private CommentAdapter mAdapter;
    private ArrayList<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_comment);
        Toolbar toolbar = findViewById(R.id.short_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String shortComment = intent.getStringExtra("short_comments");
        actionBar.setTitle(shortComment+"条短评论");
        RecyclerView shortList =findViewById(R.id.short_comment_list);
        Log.d("QWQ", "onFinish:shortComments ");
        initData();
        Log.d("QWQ1", "onFinish:shortComments ");
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        Log.d("QWQ2", "onFinish:shortComments ");
        shortList.setLayoutManager(mLinearLayoutManager);
        Log.d("QWQ3", "onFinish:shortComments ");
        mAdapter=new CommentAdapter(commentList,ShortCommentActivity.this);
        mAdapter.setHasStableIds(true);
        Log.d("QWQ4", "onFinish:shortComments ");
        shortList.setAdapter(mAdapter);
        Log.d("QWQ5", "onFinish:shortComments ");

    }

    private void initData() {
        commentList=new ArrayList<>();
        getShortCommentsFromNet();
    }

    private void getShortCommentsFromNet() {
        String url = "https://news-at.zhihu.com/api/4/story/" + id + "/short-comments";
        HttpConnect.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("QWQ", "onFinish:shortComments ");
                ParseJSONForShortComment(response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("错误", "onError: ");
                e.printStackTrace();
            }
        });
    }

    private void ParseJSONForShortComment(String JSONData) {
        try {
            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray response=jsonObject.optJSONArray("comments");
            for (int i = 0; i < response.length(); i++) {
                JSONObject item =response.optJSONObject(i);
                Comment listComment = new Comment();
                listComment.setTime(item.optLong("time"));
                listComment.setAuthor(item.optString("author"));
                listComment.setAvatar(item.optString("avatar"));
                listComment.setContent(item.optString("content"));
                listComment.setLikes(item.optInt("likes"));
                commentList.add(listComment);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();//通知适配器刷新数据
            }
        });

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
