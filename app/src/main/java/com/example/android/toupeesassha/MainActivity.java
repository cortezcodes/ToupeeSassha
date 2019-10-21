package com.example.android.toupeesassha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for news article data afrom the Guardian API
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    /**
     * the Guardian API key
     */
    private static final String API_KEY = "426f18be-1644-42b1-bdc6-f6164a6fc29e";

    /**
     * Constant Value for the NewsArticleLoader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;

    /**
     * Aadapter for the list of Articles
     */
    private NewsArticleAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article_activity);

        //Create ListView from the current layout and set the adapter
        ListView listView = (ListView) this.findViewById(R.id.article_list_view);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        mEmptyStateTextView.setText(R.string.empty_query);
        listView.setEmptyView(mEmptyStateTextView);

        //Create a NewsArticleAdapter for the ListView
        mAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

        /*
        Set the adapter on the ListView so the list can be populated in the user interface
         */
        listView.setAdapter(mAdapter);

        //set an item click listener on the ListView, which sends an intent to a web browser
        //to open a website with more information about the selected article.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the current article that was clicked on
                NewsArticle currentArticle = mAdapter.getItem(position);

                //Convert the String URL into a URI object (to  pass into the Intent constructor
                Uri articleUri = Uri.parse(currentArticle.getURL());

                //create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()){
            //Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            //Initialize the loader. Pass in the int ID constant defined above and pass in null for
            //the bundle. Pass in this activity for the LoaderCallback parameter (which is valid
            //because this activity implements the LoaderCallbacks interface.
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            //Otherwise, display error.
            mEmptyStateTextView.setText(R.string.no_internet);

        }


    }

    /**
     * Required Callback for the LoaderCallbacks implementation
     * @param id of the Loader calling back
     * @param args
     * @return
     */
    @NonNull
    @Override
    public android.content.Loader<List<NewsArticle>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section","us-news");
        uriBuilder.appendQueryParameter("from-date", "2019-09-20");
        uriBuilder.appendQueryParameter("to-date", "2019-10-20");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", "50");
        uriBuilder.appendQueryParameter("q","Donald Trump");
        uriBuilder.appendQueryParameter("api-key",API_KEY);

        Log.d("UriBuild", "Final URL: " + uriBuilder.toString());

        return new NewsArticleLoader(this, uriBuilder.toString());
    }

    /**
     * Required Callback for the LoaderCallbacks Interface
     * @param loader
     * @param newsArticles
     */
    @Override
    public void onLoadFinished(android.content.Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {

        /*
        if there is a valid list of articles, then add them to the adapter's data set.
        this will trigger the ListView to update.
         */
        if(newsArticles != null && !newsArticles.isEmpty()){
            mAdapter.addAll(newsArticles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        //Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
