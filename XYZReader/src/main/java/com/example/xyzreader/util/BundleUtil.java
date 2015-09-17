package com.example.xyzreader.util;

import android.os.Bundle;
import com.example.xyzreader.remote.Article;

import java.util.ArrayList;

public class BundleUtil {
// ------------------------------ FIELDS ------------------------------

    public static final String ARG_ARTICLES = "articles";
    public static final String ARG_ARTICLE = "article";
    public static final String ARG_SELECTED_INDEX = "selectedIndex";

// -------------------------- STATIC METHODS --------------------------

    public static Article getArticle(Bundle bundle) {
        return bundle.getParcelable(ARG_ARTICLE);
    }

    public static ArrayList<Article> getArticles(Bundle bundle) {
        return bundle.getParcelableArrayList(ARG_ARTICLES);
    }

    public static int getSelectedIndex(Bundle bundle) {
        return bundle.getInt(ARG_SELECTED_INDEX);
    }

    public static void setArticle(Bundle bundle, Article article) {
        bundle.putParcelable(ARG_ARTICLE, article);
    }

    public static void setArticles(Bundle bundle, ArrayList<Article> articles) {
        bundle.putParcelableArrayList(ARG_ARTICLES, articles);
    }

    public static void setSelectedIndex(Bundle bundle, int selectedIndex) {
        bundle.putInt(ARG_SELECTED_INDEX, selectedIndex);
    }
}
