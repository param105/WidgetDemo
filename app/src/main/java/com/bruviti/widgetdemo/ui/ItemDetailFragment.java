package com.bruviti.widgetdemo.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.bruviti.widgetdemo.R;
import com.bruviti.widgetdemo.model.entity.Article;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    Article article;
    private OnClickListener moreButtonClickListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            article = getArguments().getParcelable(getResources().getString(R.string.parcel_name));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout collapsToolBarlayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (collapsToolBarlayout != null) {
                collapsToolBarlayout.setTitle(article.getContent());
                ImageView imageView = collapsToolBarlayout.findViewById(R.id.collapsing_imageview);
                Picasso.get().load(article.getUrlToImage()).into(imageView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        if (article != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(article.getContent());
            Button moreButton = rootView.findViewById(R.id.more_button);
            moreButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String siteUrl = article.getUrl();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getContext(), Uri.parse(siteUrl));
                }
            });
        }
        return rootView;
    }

}
