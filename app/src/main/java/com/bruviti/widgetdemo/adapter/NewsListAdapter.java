package com.bruviti.widgetdemo.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bruviti.widgetdemo.ui.ItemDetailActivity;
import com.bruviti.widgetdemo.ui.ItemDetailFragment;
import com.bruviti.widgetdemo.ui.ItemListActivity;
import com.bruviti.widgetdemo.R;
import com.bruviti.widgetdemo.model.entity.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ListViewHolder> {
    public String TAG = "NewsAdapter";

    ItemListActivity parentActivity;
    LayoutInflater inflater;
    List<Article> articleArrayList;
    private View.OnClickListener itemClickListener;
    private boolean mTwoPane = false;


    public NewsListAdapter(ItemListActivity parentActivity, List<Article> dataDataModelArrayList, boolean mTwoPane){
        this.parentActivity = parentActivity;
        inflater = LayoutInflater.from(parentActivity);
        articleArrayList = dataDataModelArrayList;
        this.mTwoPane = mTwoPane;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_item_view_big, parent, false);
        setupOnClickListener();
        return new ListViewHolder(view);
    }

    private void setupOnClickListener() {
        itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Article item = (Article) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putParcelable(parentActivity.getResources().getString(R.string.parcel_name),item);

                if(mTwoPane){
                    Fragment itemDetailsFragment = new ItemDetailFragment();
                    itemDetailsFragment.setArguments(bundle);
                    parentActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.item_detail_container,itemDetailsFragment)
                            .commit();
                }else{
                    // application is running in portrait mode so No Two pane
                    Intent intent = new Intent(parentActivity, ItemDetailActivity.class);
                    intent.putExtra("bundle",bundle);
                    parentActivity.startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Article item = articleArrayList.get(position);
        Picasso.get().load(articleArrayList.get(position).getUrlToImage()).into(holder.image);
        holder.title.setText(articleArrayList.get(position).getTitle());
        holder.publishDate.setText(articleArrayList.get(position).getPublishedAt());
        holder.author.setText(articleArrayList.get(position).getAuthor());

        // set tag to view for further reference
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(itemClickListener);

    }



    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        public TextView publishDate;
        public TextView author;
        public TextView title;
        public ImageView image;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            publishDate = itemView.findViewById(R.id.published_at);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            image = itemView.findViewById(R.id.detail_image);
        }
    }
}
