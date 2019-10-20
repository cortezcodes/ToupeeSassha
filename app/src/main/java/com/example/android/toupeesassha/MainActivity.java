package com.example.android.toupeesassha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article_activity);

        //Create a ArrayList of NewsArticles to be used in the ListView
        ArrayList<NewsArticle> articles = new ArrayList<>();

        //Add articles to articles ArrayList
        articles.add(new NewsArticle("Trump Article 1", "Http", "10/20/2019", "LifeStyle"));
        articles.add(new NewsArticle("Trump Article 2", "Http", "10/20/2018", "Gaming"));
        articles.add(new NewsArticle("Trump Article 3", "Http", "10/21/2019", "World"));
        articles.add(new NewsArticle("Trump Article 4", "Http", "10/20/2017", "LifeStyle"));
        articles.add(new NewsArticle("Trump Article 5", "Http", "10/20/2016", "Government"));
        articles.add(new NewsArticle("Trump Article 6", "Http", "10/20/2019", "LifeStyle"));
        articles.add(new NewsArticle("Trump Article 7", "Http", "10/20/2019", "LifeStyle"));
        articles.add(new NewsArticle("Trump Article 8", "Http", "10/20/2019", "LifeStyle"));
        articles.add(new NewsArticle("Trump Article 9", "Http", "10/20/2019", "LifeStyle"));
        articles.add(new NewsArticle("Trump Article 10", "Http", "10/20/2019", "LifeStyle"));

        //Create a NewsArticleAdapter for the ListView
        NewsArticleAdapter adapter = new NewsArticleAdapter(this, articles);

        //Create ListView from the current layout and set the adapter
        ListView listView = (ListView) this.findViewById(R.id.article_list_view);
        listView.setAdapter(adapter);
    }
}
