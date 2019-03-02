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
import android.widget.TextView;

import com.example.gityim.wintereaxmination.Adapter.CommentAdapter;
import com.example.gityim.wintereaxmination.bean.Comment;
import com.example.gityim.wintereaxmination.http.HttpCallbackListener;
import com.example.gityim.wintereaxmination.http.HttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    private ArrayList<Comment> mComments;
    private String id;
    private CommentAdapter mAdapter;
    String Scomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent =getIntent();
        String comments = intent.getStringExtra("Comment");
        String Lcomment = intent.getStringExtra("long_comment");
        Scomment = intent.getStringExtra("short_comment");
        String popularity = intent.getStringExtra("popularity");
        id = intent.getStringExtra("id");
        Log.d("接受", " "+Lcomment+id+popularity+comments+Scomment);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);
        String title = Lcomment + "条长评论";
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(title);
        //initData
        mComments = new ArrayList<>();
        getCommentFromNet();
        //initView
        RecyclerView mRecyclerview = findViewById(R.id.comment_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        
        mAdapter = new CommentAdapter(mComments, CommentActivity.this);
        mAdapter.setHasStableIds(true);
        mRecyclerview.setAdapter(mAdapter);
        //设置点赞的点击事件
    }

    private void getCommentFromNet() {
        String url = "https://news-at.zhihu.com/api/4/story/" + id + "/long-comments";
        HttpConnect.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("QWQ", "onFinish:DD ");
                ParseJSONForComment(response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("错误", "onError: ");
                e.printStackTrace();
            }
        });
    }

    private void ParseJSONForComment(String JSONData) {
        try {
            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray JSONcomments = jsonObject.optJSONArray("comments");
            for (int i = 0; i < JSONcomments.length(); i++) {
                JSONObject item = JSONcomments.optJSONObject(i);
                Comment listComment = new Comment();
                listComment.setTime(item.optLong("time"));
                listComment.setAuthor(item.optString("author"));
                listComment.setAvatar(item.optString("avatar"));
                listComment.setContent(item.optString("content"));
                listComment.setLikes(item.optInt("likes"));
                mComments.add(listComment);
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
            case R.id.short_item:
                Intent intent = new Intent(CommentActivity.this,ShortCommentActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("short_comments",Scomment);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {//允许菜单显示出来
        getMenuInflater().inflate(R.menu.long_menu, menu);
        return true;
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mAdapter.notifyDataSetChanged();//通知适配器刷新数据
            }
        });

    }


}
