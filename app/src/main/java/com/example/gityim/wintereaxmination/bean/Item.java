package com.example.gityim.wintereaxmination.bean;

import android.support.v7.widget.RecyclerView;

//1.属性必须私有化2.必有一个无参的构造函数3，私有属性通过public方法共享给其他程序，遵行一定的命名规范
public class Item {
    private String headTitle = null;
    private String id;
    private String title;
    private String date;
    private String picurl;

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public Item() {
    }
}
