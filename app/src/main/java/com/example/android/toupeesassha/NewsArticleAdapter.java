package com.example.android.toupeesassha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom ArrayAdapter class to populate NewsArticle objects to inflate a ListView
 */
public class NewsArticleAdapter extends ArrayAdapter <NewsArticle>{


    /**
     * Custom constructor. The context is used to inflate the layout file, and the list is
     * the data we want to populate into the list
     * @param context The context that will inflate the layout file
     * @param articleList list of NewsArticle objects to display in the list
     */
    public NewsArticleAdapter(@NonNull Context context, ArrayList<NewsArticle> articleList){
        super(context, 0, articleList);
    }

    /**
     * Provides a view for the ListView.
     * @param position The position in the ArrayList that should be displayed.
     * @param convertView The recycle view to inflate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return the view to inflate
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Retrieve the recycled view to populate if one has already been created
        View listItemView = convertView;

        //If there is no previously recycled view create a new view
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_article_item, parent, false);
        }

        //Get the NewsArticle objected at the given postion
        NewsArticle currentArticle = getItem(position);

        //Find each view in the inflated layout and populated with the articles information
        TextView titleTextView = listItemView.findViewById(R.id.article_title_text_view);
        titleTextView.setText(currentArticle.getTitle());

        TextView sectionTextView = listItemView.findViewById(R.id.article_section_text_view);
        sectionTextView.setText(currentArticle.getSection());

        TextView dateTextView = listItemView.findViewById(R.id.article_publish_date_text_view);
        dateTextView.setText(currentArticle.getPublishDate());

        return listItemView;
    }
}
