package com.example.gityim.wintereaxmination;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsInfoActivity extends AppCompatActivity {
    public static final String NEWS_ID = "id";
    public static final String NEWS_TITLE = "title";
    public static final String IMG_URL = "imgUrl";
    String id;
    private WebView newsWebView;
    private String popularity;
    private String Lcomment;
    private String Scomment;
    private String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_news_info);
        Intent intent = getIntent();
        id = intent.getStringExtra(NEWS_ID);
        String title = intent.getStringExtra(NEWS_TITLE);
        String imgUrl = intent.getStringExtra(IMG_URL);

        String newsUrl = "https://news-at.zhihu.com/api/4/news/" + id;
        Toolbar toolbar = findViewById(R.id.newsHead_toolbar);
        //CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        //ImageView newsImageView = findViewById(R.id.newsHead_image);
        newsWebView = findViewById(R.id.news_webView);
        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsWebView.setWebViewClient(new WebViewClient());
        String commentUrl="https://zhihu-daily.leanapp.cn/api/v1/contents/extra/"+id;
        HttpConnect.sendHttpRequest(commentUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("成功", "onFinish: ");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject extradata=jsonObject.optJSONObject("DES");
                    popularity = extradata.optString("popularity");
                    comment = extradata.optString("comments");
                    Lcomment=extradata.optString("long_comments");
                    Scomment=extradata.optString("short_comments");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        HttpConnect.sendHttpRequest(newsUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("DD1", "onFinish: ");
                String shareUrl = parseJSONWithJSONObject(response);
                showResponse(shareUrl);
            }

            @Override
            public void onError(Exception e) {
                Log.d("DDD", "onError: ");
                e.printStackTrace();
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("");
        //Glide.with(this).load(imgUrl).into(newsImageView);


    }

    public boolean onCreateOptionsMenu(Menu menu) {//允许菜单显示出来

//        final TextView upView =findViewById(R.id.up_text);
//        final TextView commentView=findViewById(R.id.comment_view);
//        String commentUrl="https://zhihu-daily.leanapp.cn/api/v1/contents/extra/"+id;
//
//        HttpConnect.sendHttpRequest(commentUrl, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                Log.d("成功", "onFinish: ");
//                try {
//                    JSONObject extradata=new JSONObject(response);
//                    String popularity =extradata.optString("popularity");
//                    String Comment=extradata.optString("comments");
//                    upView.setText(popularity);
//                    commentView.setText(Comment);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            @Override
//            public void onError(Exception e) {
//                Log.d("错误", "onError: ");
//                e.printStackTrace();
//            }
//        });
        getMenuInflater().inflate(R.menu.mainnews, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.thumb_up:
                break;
            case R.id.share:
                break;
            case R.id.collection:
                break;
            case R.id.comment:
                Intent intent = new Intent(NewsInfoActivity.this, CommentActivity.class);
                intent.putExtra("popularity",popularity);
                intent.putExtra("Comment",comment);
                intent.putExtra("long_comment",Lcomment);
                intent.putExtra("short_comment",Scomment);
                intent.putExtra("id",id);
                Log.d("发出", ""+popularity+" "+comment+" "+Lcomment+" "+Scomment+" "+id);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String parseJSONWithJSONObject(String JSONData) {
        Gson gson = new Gson();
        NewsContent content = gson.fromJson(JSONData, NewsContent.class);
        String share_url = content.getShare_url();
//        final ImageLoader imageloader = ImageLoader.getInstance();
//        imageloader.displayImage(content.getImage());
//        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
//        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
//        html = html.replace("<div class=\"img-place-holder\">", "");
//        newsWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        Log.d("测试", "" + share_url);
        return share_url;
    }

    private void showResponse(final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsWebView.loadUrl(url);
            }
        });
    }
}
