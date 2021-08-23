package com.example.bitshorts;

public class NewsCard {

    private String url;
    private String title;
    private String author;
    private String date;
    private String source;

    public NewsCard(String mUrl, String mTitle, String mAuthor, String mDate, String mSource){
            url = mUrl;
            title = mTitle;
            author = mAuthor;
            date = mDate;
            source = mSource;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }
}
