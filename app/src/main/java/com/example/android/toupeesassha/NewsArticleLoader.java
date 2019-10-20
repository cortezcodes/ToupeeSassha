package com.example.android.toupeesassha;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Loads a list of NewsArticles by using an AsyncTaskLoader to perfom the network request to the
 * given URL.
 */
public class NewsArticleLoader extends AsyncTaskLoader {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsArticleLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new NewsArticleLoader
     *
     * @param context
     * @param url
     */
    public NewsArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     * @return
     */
    @Nullable
    @Override
    public List<NewsArticle> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        //Perform the network request, parse the response, and extract a list of articles
        List<NewsArticle> articles = QueryUtility.fetchNewsArticleData(mUrl);
        return articles;
    }
}
