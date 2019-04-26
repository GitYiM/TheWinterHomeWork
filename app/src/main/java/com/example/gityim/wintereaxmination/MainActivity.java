package com.example.gityim.wintereaxmination;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gityim.wintereaxmination.Adapter.InfoListAdapter;
import com.example.gityim.wintereaxmination.bean.Item;
import com.example.gityim.wintereaxmination.http.HttpCallbackListener;
import com.example.gityim.wintereaxmination.http.HttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.bingoogolapple.bgabanner.BGABanner;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements RecyclerClickListener {
    private RecyclerView mInfoList;
    private ArrayList<Item> mDatas;
    private InfoListAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    //banner
    private ArrayList<Item> bannerList;
    private ArrayList<String> bantitles;
    private ArrayList<String> banimgs;
    private ArrayList<String> banids;
    private int otherDate = -1;

    //运行时权限
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private CircleImageView photoView;
    private TextView userName;

    //实现viewPager
//    private ViewPager viewPager;
//    private ArrayList<View> pagerList;

    //弹窗
//    PopupWindow popupWindow;
//    Button takePhoto;
//    Button canBtn;
//    Button albumBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.head_toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);

        //toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);//标题栏中字体变为白色
        //设置最左上角导航按钮
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); //改变图标
        }
        navigationView.setCheckedItem(R.id.top_stories);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.top_stories) {
                    Intent intent = new Intent(MainActivity.this, FireNewsActivity.class);
                    startActivity(intent);
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //initdata()部分
        mDatas = new ArrayList<>();
        getInfoFromNet();

        //initview()部分

        mInfoList = findViewById(R.id.info_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mInfoList.setLayoutManager(mLinearLayoutManager);
        mAdapter = new InfoListAdapter(mDatas, MainActivity.this);
        mInfoList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        //实现轮播图
        initbannar();

        //实现下拉刷新
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshinfo();
                swipeRefresh.setRefreshing(false);
            }
        });

        //实现上拉加载更多
        mInfoList.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager) {
            @Override
            protected void onLoadMore() {
                loadMoreData();

            }
        });
        //换navigationview中的头像
        NavigationView mNavi = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        photoView = headerView.findViewById(R.id.icon_image);
        userName = headerView.findViewById(R.id.login_user_name);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity.this));
                dialog.setTitle("Choose the way to change headPhoto");
                dialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }

                    }
                });
                dialog.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File outputImage = new File(getExternalCacheDir(), "output_Image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.wintereaxmination.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        Log.d("闪退2", "onClick: ");
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                });
                dialog.show();

            }
        });
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("郁闷", "onClick: ");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });
        Intent intent1 = getIntent();
        Log.d("主活动返回值", "onCreate: " + intent1.getStringExtra("Username"));
        if (intent1.getStringExtra("Username") != null) {
            userName.setText(intent1.getStringExtra("Username"));
        }

        //popupWindow点击事件失败
//        Log.d("闪退3", "onClick: ");
//        takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("闪退1", "onClick: ");
//
//            }
//        });
        //实现viewpager

        //initdata()
//        pagerList= new ArrayList<>();
//        LayoutInflater li = getLayoutInflater();
//        pagerList.add(li.inflate(R.layout.view_one,null,false));
//        pagerList.add(li.inflate(R.layout.view_two,null,false));
//        pagerList.add(li.inflate(R.layout.view_three,null,false));
//        viewPager =findViewById(R.id.view_pager_view);
//        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(pagerList);
//        viewPager.setAdapter(pagerAdapter);


    }



    //拍照回调
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        photoView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    //    protected void onActivityResult(int RequestCode,int ResultCode,Intent data) {
//
//    }


    private void loadMoreData() {
        otherDate = otherDate+1;
        Log.d("事件", "loadMoreData: "+otherDate);
        getInfoFromNet();
    }

    //获取网络数据方法
    private void getInfoFromNet() {
        String url = null;
        if (otherDate == -1) {
            url = "http://news-at.zhihu.com/api/4/news/latest";
        } else {
            url = "http://news-at.zhihu.com/api/4/news/before/" + getDate();
            Log.d("事件", "getInfoFromNet: "+getDate());
        }
        HttpConnect.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("成功", "onFinish: ");
                parseJSONWithJSONObject(response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("网络请求错误", "onError: ");
                e.printStackTrace();
            }
        });

    }

    //获取日期
    private String getDate() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        currentDate.add(Calendar.DAY_OF_MONTH, -(otherDate));//other天前的日子
        String date = new SimpleDateFormat("yyyyMMdd").format(currentDate.getTime());// 规定日期格式
        return date;
    }
    private String getDate1() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        currentDate.add(Calendar.DAY_OF_MONTH, -(otherDate+1));//other天前的日子
        String date = new SimpleDateFormat("yyyyMMdd").format(currentDate.getTime());// 规定日期格式
        return date;
    }


    private void parseJSONWithJSONObject(String JSONData) {
        try {

            JSONObject response = new JSONObject(JSONData);
            JSONArray list = null;
            list = response.optJSONArray("stories");
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.optJSONObject(i);
                JSONArray images = item.optJSONArray("images");
                Item listItem = new Item();
                //创建list每一个对象，并封装
                if (i == 0) {
                    listItem.setHeadTitle(strToDateFormat(getDate1()));
                }
                listItem.setTitle(item.optString("title"));
                listItem.setPicurl(images.optString(0));
                listItem.setDate(getDate());
                listItem.setId(item.optString("id"));
                mDatas.add(listItem);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {    //允许菜单显示出来
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {  //menu的响应事件
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.night_item:
                break;
            case R.id.setting_item:
                Intent intent = new Intent(MainActivity.this, SetOptionActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Item item = mDatas.get(position);
        String id = item.getId();
        String title = item.getTitle();
        String imgUrl = item.getPicurl();
        Intent intent = new Intent(MainActivity.this, NewsInfoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(intent);
    }

    //刷新数据方法
    private void refreshinfo() {
        otherDate =-1;
        mDatas.clear();
        String url = null;
        url = "http://news-at.zhihu.com/api/4/news/latest";
        HttpConnect.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("成功", "onFinish: ");
                parseJSONWithJSONObject(response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("错误", "onError: ");
                e.printStackTrace();
            }
        });


    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();//通知适配器刷新数据
            }
        });

    }

    public ArrayList<Item> dataReset() {
        ArrayList<Item> newmDatas = new ArrayList<>();
        return newmDatas;
    }

    //展示弹窗
//    private void showNoneEffect() {
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View vPopupWindow = inflater.inflate(R.layout.popwindow, null, false);//引入弹窗布局
//        popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
//        View parentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
//        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//        popupWindow.setTouchable(false);
//        takePhoto=vPopupWindow.findViewById(R.id.take_btn);
//        canBtn=vPopupWindow.findViewById(R.id.cancel_btn);
//        albumBtn=vPopupWindow.findViewById(R.id.album_btn);
//    }
    //相册获取图片
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            photoView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public static String strToDateFormat(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }
    private void initbannar() {
        bantitles = new ArrayList<>();
        banids = new ArrayList<>();
        banimgs = new ArrayList<>();
        bannerList = new ArrayList<>();
        String url = "http://news-at.zhihu.com/api/4/news/latest";
        HttpConnect.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("成功", "onFinish: ");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray topinfos = jsonObject.getJSONArray("top_stories");
                    Log.d("TAG", "onResponse: "+topinfos);
                    for (int i = 0; i < topinfos.length(); i++) {
                        JSONObject item = topinfos.getJSONObject(i);
                        Item item1 = new Item();
                        item1.setPicurl(item.getString("image"));
                        item1.setTitle(item.getString("title"));
                        item1.setId(item.getString("id"));
                        bannerList.add(item1);
                        bantitles.add(item1.getTitle());
                        banimgs.add(item1.getPicurl());
                        banids.add(item1.getId());
                    }


                    setHeader(mInfoList, banimgs, bantitles, banids);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("网络请求错误", "onError: ");
                e.printStackTrace();
            }
        });

    }

    private void setHeader(RecyclerView view, ArrayList<String> urls, ArrayList<String> titles, final ArrayList<String> ids) {
        View header = LayoutInflater.from(this).inflate(R.layout.headview, view, false);
        //找到banner所在的布局
        BGABanner banner = (BGABanner) header.findViewById(R.id.banner);
        //绑定banner
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {


            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(MainActivity.this)
                        .load(model)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                //此处可设置banner子项的点击事件
                Item item = bannerList.get(position);
                String id = item.getId();
                String title = item.getTitle();
                String imgUrl = item.getPicurl();
                Intent intent = new Intent(MainActivity.this, NewsInfoActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("imgUrl", imgUrl);
                startActivity(intent);
            }
        });
        banner.setData(urls, titles);
        mAdapter.setHeadView(header);
    }
}





