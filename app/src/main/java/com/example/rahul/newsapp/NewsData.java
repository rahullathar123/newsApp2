package com.example.rahul.newsapp;

/**
 * Created by rahul on 2017-09-30.
 */

public class NewsData {

    private String sectionBelong;
    private String articleName;
    private String webURL;
    private String date;
    private String author;
    // generate constructor to initialize strings

    public NewsData(String sectionBelong, String articleName, String webURL, String date, String author) {
        this.sectionBelong = sectionBelong;
        this.articleName = articleName;
        this.webURL = webURL;
        this.date = date;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
    // to get variables we genrate getters
    public String getSectionBelong() {
        return sectionBelong;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getWebURL() {
        return webURL;
    }

    public String getDate() {
        return date;
    }
}