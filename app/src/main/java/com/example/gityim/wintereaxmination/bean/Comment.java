package com.example.gityim.wintereaxmination.bean;

public class Comment {

    /**
     * author : 坑灰易冷
     * content : 谁说孟婆一定是给你做喝的“汤”的——孟婆怎么就不能是开浴池的呢？话说人到了孟婆这里都得扒光衣服泡个热水澡，代表涤荡尘埃、抛却尘世间的一切牵挂。然后洗美了烫舒服了一不留神就喝了口洗澡水——顿时大脑一片空白，迷迷糊糊把所有衣服连带全部盘缠（川资）都忘在了孟婆那里，所以日久天长那个地方就叫“忘川”……编不下去了。
     * avatar : http://pic4.zhimg.com/01d9e29ae2ff6c4f973a5a7c7b93a73b_im.jpg
     * time : 1479704382
     * id : 27275789
     * likes : 26
     */

    private String author;
    private String content;
    private String avatar;
    private Long time;
    private int id;
    private int likes;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
