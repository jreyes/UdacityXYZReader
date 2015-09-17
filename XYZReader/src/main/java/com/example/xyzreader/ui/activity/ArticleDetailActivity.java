package com.example.xyzreader.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.example.xyzreader.R;
import com.example.xyzreader.remote.Article;
import com.example.xyzreader.ui.adapter.ArticleDetailAdapter;

import java.util.ArrayList;

import static com.example.xyzreader.util.BundleUtil.*;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {
// ------------------------------ FIELDS ------------------------------

    private ArrayList<Article> mArticles;
    private int mSelectedIndex;

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        initViewPager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setArticles(outState, mArticles);
        setSelectedIndex(outState, mSelectedIndex);
    }

    private void initViewPager() {
        mArticles = getArticles(getIntent().getExtras());
        mSelectedIndex = getSelectedIndex(getIntent().getExtras());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(mArticles.size());
        viewPager.setAdapter(new ArticleDetailAdapter(getSupportFragmentManager(), mArticles));
        viewPager.setCurrentItem(mSelectedIndex);
    }
}
