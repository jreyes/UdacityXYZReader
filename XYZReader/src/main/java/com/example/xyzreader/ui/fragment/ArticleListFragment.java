package com.example.xyzreader.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.remote.Article;
import com.example.xyzreader.ui.adapter.ArticleAdapter;
import com.example.xyzreader.util.ArticleUtil;

import java.util.ArrayList;

import static com.example.xyzreader.data.UpdaterService.BROADCAST_ACTION_STATE_CHANGE;
import static com.example.xyzreader.util.BundleUtil.getArticles;
import static com.example.xyzreader.util.BundleUtil.setArticles;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
// ------------------------------ FIELDS ------------------------------

    BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                boolean isRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI(isRefreshing);
            }
        }
    };

    private ArticleAdapter mAdapter;
    private ArrayList<Article> mArticles;
    private boolean mIsRefreshing;
    private SwipeRefreshLayout mSwipeRefreshLayout;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface LoaderCallbacks ---------------------

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(getActivity());
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

// -------------------------- OTHER METHODS --------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setArticles(outState, mArticles);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mRefreshingReceiver, new IntentFilter(BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mRefreshingReceiver);
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mArticles = new ArrayList<>();
            getLoaderManager().initLoader(0, null, this);
        } else {
            mArticles = getArticles(savedInstanceState);
        }

        initSwipeRefreshLayout(view);
        initRecyclerView(view);
        initRefresh(savedInstanceState);
    }

    private void initRecyclerView(View view) {
        // create the adapter
        mAdapter = new ArticleAdapter(getActivity(), mArticles);
        // now create the RecyclerView
        ((RecyclerView) view.findViewById(R.id.recycler_view)).setAdapter(mAdapter);
    }

    private void initRefresh(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
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
            getActivity().startService(new Intent(getActivity(), UpdaterService.class));
        }
    }

    private void updateRefreshingUI(boolean isRefreshing) {
        mIsRefreshing = isRefreshing;
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }
}
