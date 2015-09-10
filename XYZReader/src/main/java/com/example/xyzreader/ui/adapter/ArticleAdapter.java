package com.example.xyzreader.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xyzreader.R;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.util.ArticleUtil;

import java.util.ArrayList;

import static android.content.Intent.ACTION_VIEW;
import static com.example.xyzreader.data.ItemsContract.Items.CONTENT_ITEM_TYPE;
import static com.example.xyzreader.util.BundleUtil.setArticles;
import static com.example.xyzreader.util.BundleUtil.setSelectedIndex;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
// ------------------------------ FIELDS ------------------------------

    private Context mContext;
    private ArrayList<Article> mItems;

// --------------------------- CONSTRUCTORS ---------------------------

    public ArticleAdapter(Context context, ArrayList<Article> items) {
        mContext = context;
        mItems = items;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mItems.get(position);
        holder.titleView.setText(article.title);
        holder.subtitleView.setText(ArticleUtil.getSubtitle(article));
        Glide.with(mContext)
                .load(article.photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.thumbnailView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                setArticles(bundle, mItems);
                setSelectedIndex(bundle, vh.getAdapterPosition());

                mContext.startActivity(new Intent(ACTION_VIEW).setType(CONTENT_ITEM_TYPE).putExtras(bundle));
            }
        });
        return vh;
    }

    public void setItems(ArrayList<Article> items) {
        mItems = items;
        notifyDataSetChanged();
    }

// -------------------------- INNER CLASSES --------------------------

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }
}
