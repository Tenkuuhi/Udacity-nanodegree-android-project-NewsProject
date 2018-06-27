package com.example.android.newsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News>  {

    public NewsAdapter(Context context, ArrayList<News> words) {

        super(context, 0, words);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID news_title and set with currentNews item.
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.news_title);
        titleTextView.setText(currentNews.getTitle());
        // Find the TextView in the list_item.xml layout with the ID news_section and set with currentNews item.
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.news_section);
        sectionTextView.setText(currentNews.getSection());
        // Find the TextView in the list_item.xml layout with the ID news_date and set with currentNews item.
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.news_date);
        dateTextView.setText(currentNews.getDate());
        // Find the TextView in the list_item.xml layout with the ID news_author and set with currentNews item.
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.news_author);
        authorTextView.setText(currentNews.getAuthor());
        return listItemView;
    }
}