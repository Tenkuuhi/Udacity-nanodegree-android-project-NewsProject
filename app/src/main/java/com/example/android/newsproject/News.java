package com.example.android.newsproject;

public class News {

    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;
    private String mUrl;

    public News(String title, String section, String author, String data, String url){

        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = data;
        mUrl = url;
    }

    public String getTitle() {return mTitle;}

    public String getSection() {return mSection;}

    public String getAuthor() {return mAuthor;}

    public String getDate() {return mDate;}

    public String getUrl() {return mUrl;}
}