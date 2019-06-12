package com.bruviti.widgetdemo.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.bruviti.widgetdemo.R;
import com.bruviti.widgetdemo.adapter.NewsListAdapter;
import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.entity.JsonResponseObject;
import com.bruviti.widgetdemo.util.ApiService;
import com.bruviti.widgetdemo.util.RetrofitClient;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static String TAG ="ItemListActivity";
    private boolean mTwoPane = false;
    ArrayList<Article> articleList = new ArrayList<Article>();
    View recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

         recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        fetchNews();


    }

    /***
     * this method will fetch JSON data from newsApi using retroFit.
     */
    private void fetchNews() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<JsonResponseObject> newsDaoCall = apiService.getNewsListJSON();

        newsDaoCall.enqueue(new Callback<JsonResponseObject>() {
            @Override
            public void onResponse(Call<JsonResponseObject> call, Response<JsonResponseObject> response) {
                JsonResponseObject responsedata = response.body();
                Log.d(TAG,"Response received is :- "+ response.body().toString());

                if(response.isSuccessful()){
                    articleList.addAll(responsedata.getArticles());
                    setupRecyclerView((RecyclerView) recyclerView);
                    Log.d(TAG,"Response received is :- "+ articleList.toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Fetching issue", LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonResponseObject> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        NewsListAdapter recyclerAdapter = new NewsListAdapter(this,articleList,mTwoPane);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }


}
