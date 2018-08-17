package com.example.anhdinh.newsapp;

public class Article {
    // Create variables to show on ListView
    private String mArticleTitle;
    private String mArticleSection;
    private String mArticleDatePublished;
    private String mAuthorName;
    private String mUrl;

    // This constructor is for articles which have author's name


    public Article(String mArticleTitle, String mArticleSection, String mArticleDatePublished, String mAuthorName, String mUrl) {
        this.mArticleTitle = mArticleTitle;
        this.mArticleSection = mArticleSection;
        this.mArticleDatePublished = mArticleDatePublished;
        this.mAuthorName = mAuthorName;
        this.mUrl = mUrl;
    }

    public Article(String mArticleTitle, String mArticleSection, String mArticleDatePublished, String mUrl) {
        this.mArticleTitle = mArticleTitle;
        this.mArticleSection = mArticleSection;
        this.mArticleDatePublished = mArticleDatePublished;
        this.mUrl = mUrl;
    }

    public String getmArticleTitle() {
        return mArticleTitle;
    }

    public String getmArticleDatePublished() {
        return mArticleDatePublished;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmArticleSection() {
        return mArticleSection;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }
}
