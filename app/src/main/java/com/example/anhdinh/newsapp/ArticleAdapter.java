package com.example.anhdinh.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article>{

    // Set the constructor to have 2 inputs
    public ArticleAdapter(@NonNull Context context, ArrayList<Article> articles) {
        super(context, 0,articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get position of the item:
        Article currentArticle = getItem(position);

        // Create TextView for the ArticleTitle, ArticleDatePublished, ArticleSection, and AuthorName
        TextView artticleTitle = (TextView) listItemView.findViewById(R.id.article_title);
        artticleTitle.setText(currentArticle.getmArticleTitle());

        TextView articleSection = (TextView) listItemView.findViewById(R.id.article_section);
        articleSection.setText(currentArticle.getmArticleSection());

        TextView articleDatePublished = listItemView.findViewById(R.id.article_date_published);
        articleDatePublished.setText(currentArticle.getmArticleDatePublished());

        TextView authorName = listItemView.findViewById(R.id.author_name);
        authorName.setText(currentArticle.getmAuthorName());

        return listItemView;
    }
}
