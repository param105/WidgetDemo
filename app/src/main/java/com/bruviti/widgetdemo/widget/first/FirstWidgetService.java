package com.bruviti.widgetdemo.widget.first;

import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.bruviti.widgetdemo.R;
import com.bruviti.widgetdemo.model.NewsAppContentProvider;
import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.entity.JsonResponseObject;
import com.bruviti.widgetdemo.util.ApiService;
import com.bruviti.widgetdemo.util.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static com.bruviti.widgetdemo.widget.first.FirstAppWidgetProvider.EXTRA_ITEM_POSITION;

public class FirstWidgetService extends RemoteViewsService  {
    public static final int LOADER_NEWS = 1;
    public static final String TAG = "FirstWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FirstWidgetViewsFactory(this,intent);
    }


    class FirstWidgetViewsFactory implements RemoteViewsFactory  {
        Context context;
        private int appWidgetId;
        LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks;

        private ArrayList<Article> articleList = new ArrayList<Article>();

        public FirstWidgetViewsFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {
           // fetchNewsDirectlyFromCloud();
            fetchNewsFromContentProvider();
            SystemClock.sleep(1000);
         //   LoaderManager.getInstance(this).initLoader(1,null,mLoaderCallbacks);


            mLoaderCallbacks =
                    new LoaderManager.LoaderCallbacks<Cursor>() {

                        @Override
                        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                            switch (id) {
                                case LOADER_NEWS:
                                    return new CursorLoader(getApplicationContext(),
                                            NewsAppContentProvider.BASE_CONTENT_URI,
                                            null,
                                            null, null, null);
                                default:
                                    throw new IllegalArgumentException();
                            }
                        }

                        @Override
                        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                            switch (loader.getId()) {
                                case LOADER_NEWS:

                                    List<Article> articles = getArticleObjectFromCursorData(data);
                                    articleList.addAll(articles);
                                    FirstAppWidgetProvider.sendRefreshBroadcast(context);
                                    break;
                            }
                        }

                        @Override
                        public void onLoaderReset(Loader<Cursor> loader) {
                            switch (loader.getId()) {
                                case LOADER_NEWS:
                                    articleList.addAll(null);
                                    break;
                            }
                        }

                    };
        }



        @Override
        public void onDataSetChanged() {
            fetchNewsFromContentProvider();
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

        /**
         * Get data form content provider
         */
        private void fetchNewsFromContentProvider() {

            new Thread(
            new Runnable() {
                @Override
                public void run() {
                    Uri uri = ContentUris.
                            withAppendedId(NewsAppContentProvider.BASE_CONTENT_URI, 1);

                    String[] projection = { Article.COLUMN_ID,
                            Article.COLUMN_TITLE,
                            Article.COLUMN_AUTHER,
                            Article.COLUMN_DESCRIPTION
                    };

                    String selection = null;
                    String[] selectionArgs = null;
                    String sortOrder = Article.COLUMN_TITLE + "ASC";

                    Cursor cursor = context.getApplicationContext()
                            .getContentResolver()
                            .query(uri, projection, selection, selectionArgs, sortOrder);

                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        List<Article> articles = getArticleObjectFromCursorData(cursor);
                        articleList.addAll(articles);
                        FirstAppWidgetProvider.sendRefreshBroadcast(context);

                        Log.d(TAG, "fetchNewsFromContentProvider: "+articles);
                    } else {
                        Log.d(TAG, "fetchNewsFromContentProvider: Cursor is empty");

                    }

                    cursor.close();
                }
            }).start();

        }

        /**
         * function to extract data form cursor
         * where it will create article object and will pass to adapter
         * @param cursor
         * @return
         */
        private List<Article> getArticleObjectFromCursorData(Cursor cursor) {
            ArrayList<Article> articles = new ArrayList<Article>();

            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                      Article article = new Article();
                    article.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Article.COLUMN_TITLE)));
                    articles.add(article);

                }
            } finally {
                cursor.close();
            }

            return articles;
        }

        /***
         * this method will fetch JSON data from newsApi using retroFit.
         */
        private void fetchNewsDirectlyFromCloud() {
            ApiService apiService = RetrofitClient.getApiService();
            Call<JsonResponseObject> newsDaoCall = apiService.getNewsListJSON();

            newsDaoCall.enqueue(new Callback<JsonResponseObject>() {
                @Override
                public void onResponse(Call<JsonResponseObject> call, Response<JsonResponseObject> response) {
                    JsonResponseObject responsedata = response.body();

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
