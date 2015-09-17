package com.example.xyzreader.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.xyzreader.remote.Article;
import com.example.xyzreader.ui.fragment.ArticleDetailFragment;

import java.util.ArrayList;

public class ArticleDetailAdapter extends FragmentStatePagerAdapter {
// ------------------------------ FIELDS ------------------------------

    private ArrayList<Article> mArticles;

// --------------------------- CONSTRUCTORS ---------------------------

    public ArticleDetailAdapter(FragmentManager fm, ArrayList<Article> articles) {
        super(fm);
        mArticles = articles;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailFragment.newInstance(mArticles.get(position));
    }
}
