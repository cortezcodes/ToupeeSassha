package com.example.android.toupeesassha;

import androidx.annotation.Nullable;

/**
 * Custom Class used to store information on each news article returned from the JSON query.
 * contains all the data required to be displayed for a single article
 */
public class NewsArticle {

    private String mTitle;
    private String mURL;
    private String mPublishDate;
    private String mSection;
    private String mAuthor;

    /**
     * Constructor
     * @param title title of the news article
     * @param url url to the webpage the article was originally hosted
     * @param publishDate date article was published
     * @param section section of news the article is from
     */
    public NewsArticle(String title, String url, String publishDate, String section, @Nullable String author){
        mTitle = title;
        mURL = url;
        mPublishDate = publishDate;
        mSection = section;
        mAuthor =author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getURL() {
        return mURL;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
