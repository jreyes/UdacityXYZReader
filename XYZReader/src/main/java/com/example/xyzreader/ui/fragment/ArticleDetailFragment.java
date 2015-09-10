package com.example.xyzreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.ui.activity.ArticleDetailActivity;
import com.example.xyzreader.ui.activity.ArticleListActivity;
import com.example.xyzreader.util.ArticleUtil;
import com.example.xyzreader.util.BundleUtil;

import static com.example.xyzreader.util.BundleUtil.getArticle;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    private Article mArticle;

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
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mArticle = getArticle(getArguments());
        } else {
            mArticle = getArticle(savedInstanceState);
        }

        initArticleDetail(view);
    }

    private void initArticleDetail(View view) {
        ((TextView) view.findViewById(R.id.article_title)).setText(mArticle.title);
        ((TextView) view.findViewById(R.id.article_byline)).setText(Html.fromHtml(ArticleUtil.getByLine(mArticle)));
        //bylineView.setMovementMethod(new LinkMovementMethod());
        ((TextView) view.findViewById(R.id.article_body)).setText(Html.fromHtml(mArticle.body));

        Glide.with(getActivity()).load(mArticle.photo).crossFade().into((ImageView) view.findViewById(R.id.photo));
    }
}
