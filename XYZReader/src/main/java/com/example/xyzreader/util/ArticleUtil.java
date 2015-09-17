package com.example.xyzreader.util;

import android.database.Cursor;
import android.text.format.DateUtils;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.remote.Article;

import java.util.ArrayList;
import java.util.Date;

public class ArticleUtil {
// -------------------------- STATIC METHODS --------------------------

    public static ArrayList<Article> getArticles(Cursor cursor) {
        ArrayList<Article> matches = new ArrayList<>();
        while (cursor.moveToNext()) {
            Article article = new Article();
            article.author = cursor.getString(ArticleLoader.Query.AUTHOR);
            article.body = cursor.getString(ArticleLoader.Query.BODY);
            article.id = cursor.getString(ArticleLoader.Query._ID);
            article.photo = cursor.getString(ArticleLoader.Query.PHOTO_URL);
            article.publishedDate = new Date(cursor.getLong(ArticleLoader.Query.PUBLISHED_DATE));
            article.title = cursor.getString(ArticleLoader.Query.TITLE);
            matches.add(article);
        }

        // close cursor
        cursor.close();

        // now return the values
        return matches;
    }

    public static String getSubtitle(Article article) {
        return DateUtils.getRelativeTimeSpanString(
                article.publishedDate.getTime(),
                System.currentTimeMillis(),
                DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL)
                .toString()
                + " by "
                + article.author;
    }
}
