package com.example.xyzreader.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.ui.adapter.ArticleAdapter;
import com.example.xyzreader.util.ArticleUtil;

import java.util.ArrayList;

import static com.example.xyzreader.util.BundleUtil.*;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, AppBarLayout.OnOffsetChangedListener {
// ------------------------------ FIELDS ------------------------------

    BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                boolean isRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI(isRefreshing);
            }
        }
    };

    private ArticleAdapter mAdapter;
    private AppBarLayout mAppBarLayout;
    private ArrayList<Article> mArticles;
    private boolean mIsRefreshing;
    private SwipeRefreshLayout mSwipeRefreshLayout;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface LoaderCallbacks ---------------------

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }

        // now set the items in the adapter
        mArticles = ArticleUtil.getArticles(cursor);
        mAdapter.setItems(mArticles);
        updateRefreshingUI(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

// --------------------- Interface OnOffsetChangedListener ---------------------

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mSwipeRefreshLayout.setEnabled(i == 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        initSwipeRefreshLayout();
        initAppBarLayout();
        initRecyclerView(savedInstanceState);
        initRefresh(savedInstanceState);
    }

    @Override
    protected void onPause() {
        mAppBarLayout.removeOnOffsetChangedListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setArticles(outState, mArticles);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver, new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mRefreshingReceiver);
        super.onStop();
    }

    private void initAppBarLayout() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mArticles = new ArrayList<>();
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            mArticles = getArticles(savedInstanceState);
        }

        // create the adapter
        mAdapter = new ArticleAdapter(this, mArticles);

        // now create the RecyclerView
        ((RecyclerView) findViewById(R.id.recycler_view)).setAdapter(mAdapter);
    }

    private void initRefresh(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_primary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        if (!mIsRefreshing) {
            updateRefreshingUI(true);
            startService(new Intent(this, UpdaterService.class));
        }
    }

    private void updateRefreshingUI(boolean isRefreshing) {
        mIsRefreshing = isRefreshing;
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }
}
