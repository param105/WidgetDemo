package com.bruviti.widgetdemo.widget.first;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.bruviti.widgetdemo.R;
import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.entity.JsonResponseObject;
import com.bruviti.widgetdemo.util.ApiService;
import com.bruviti.widgetdemo.util.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static com.bruviti.widgetdemo.widget.first.FirstAppWidgetProvider.EXTRA_ITEM_POSITION;

public class FirstWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FirstWidgetViewsFactory(getApplicationContext(),intent);
    }

    class FirstWidgetViewsFactory implements RemoteViewsFactory {
        Context context;
        private int appWidgetId;
        /*private String[] exampleData = {"one", "two", "three", "four",
                "five", "six", "seven", "eight", "nine", "ten"};*/

        private ArrayList<Article> articleList = new ArrayList<Article>();

        public FirstWidgetViewsFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            fetchNews();
            SystemClock.sleep(1000);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return articleList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_widget_list_item);
            views.setTextViewText(R.id.example_widget_item_text, articleList.get(i).getTitle());

            Intent fillIntent = new Intent();
            fillIntent.putExtra(EXTRA_ITEM_POSITION, i);
            views.setOnClickFillInIntent(R.id.example_widget_item_text, fillIntent);

            SystemClock.sleep(500);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
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
                   // Log.d(TAG,"Response received is :- "+ response.body().toString());

                    if(response.isSuccessful()){
                        articleList.addAll(responsedata.getArticles());
                        FirstAppWidgetProvider.sendRefreshBroadcast(context);

                    }else{
                        Toast.makeText(getApplicationContext(),"Fetching issue", LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<JsonResponseObject> call, Throwable t) {
                   // Log.d(TAG,t.getMessage());
                }
            });
        }


    }
}
