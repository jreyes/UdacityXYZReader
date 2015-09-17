package com.example.xyzreader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.remote.Article;
import com.example.xyzreader.ui.activity.ArticleDetailActivity;
import com.example.xyzreader.ui.activity.ArticleListActivity;
import com.example.xyzreader.util.ArticleUtil;
import com.example.xyzreader.util.BundleUtil;

import static com.example.xyzreader.util.BundleUtil.getArticle;
import static com.example.xyzreader.util.BundleUtil.setArticle;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    View.OnClickListener mShareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(getString(R.string.action_share_text, mArticle.title))
                    .getIntent(), getString(R.string.action_share)));
        }
    };

    private Article mArticle;
    private Toolbar mToolbar;

// -------------------------- STATIC METHODS --------------------------

    public static ArticleDetailFragment newInstance(Article article) {
        Bundle arguments = new Bundle();
        BundleUtil.setArticle(arguments, article);

        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setArticle(outState, mArticle);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mArticle = getArticle(getArguments());
        } else {
            mArticle = getArticle(savedInstanceState);
        }
        initToolbar(view);
        initArticleDetail(view);
    }

    private void initArticleDetail(View view) {
        mToolbar.setTitle(mArticle.title);

        ((TextView) view.findViewById(R.id.article_title)).setText(mArticle.title);
        ((TextView) view.findViewById(R.id.article_subtitle)).setText(Html.fromHtml(ArticleUtil.getSubtitle(mArticle)));
        ((TextView) view.findViewById(R.id.article_body)).setText(Html.fromHtml(mArticle.body));

        Glide.with(getActivity()).load(mArticle.photo).into((ImageView) view.findViewById(R.id.photo));

        view.findViewById(R.id.article_share).setOnClickListener(mShareOnClickListener);
    }

    private void initToolbar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }
}
