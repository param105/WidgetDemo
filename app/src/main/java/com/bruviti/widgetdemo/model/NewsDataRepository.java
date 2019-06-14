package com.bruviti.widgetdemo.model;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruviti.widgetdemo.model.database.NewsDatabase;
import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.entity.JsonResponseObject;
import com.bruviti.widgetdemo.model.roomdao.ArticleDao;
import com.bruviti.widgetdemo.util.ApiService;
import com.bruviti.widgetdemo.util.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class NewsDataRepository {
    public static String TAG="NewsDataRepository";

    public NewsDatabase newsDatabase;
    public ArticleDao articleDao;

    public NewsDataRepository(Context context){
        newsDatabase = NewsDatabase.getDatabase(context);
        articleDao = newsDatabase.articleDao();
    }

    /**
     * fetch articles from database
     * @return
     */
    public LiveData<List<Article>> getAllArticles(){
        return articleDao.getAllArticles();
    }

    /**
     * fetch articles from database
     * @return
     */
    public Cursor getAllArticlesCursor(){
        return articleDao.getCursorForArticles();
    }

    /**
     * insert articles in the database
     * @param articles
     */
    public void insertArticals(List<Article> articles){
         new InsertArticlesAsyncTask(articleDao).execute(articles);
    }

    /**
     * to Feth news feeds from cloud using rerofit.
     *
     */
    public void fetchNewsFromWebAPI(){
        ApiService apiService = RetrofitClient.getApiService();
        Call<JsonResponseObject> newsDaoCall = apiService.getNewsListJSON();

        newsDaoCall.enqueue(new Callback<JsonResponseObject>() {
            @Override
            public void onResponse(Call<JsonResponseObject> call, Response<JsonResponseObject> response) {
                JsonResponseObject responsedata = response.body();
                Log.d(TAG,"Response received is :- "+ response.body().toString());

                if(response.isSuccessful()){
                    //newsFeedsArticals.addAll(responsedata.getArticles());
                    insertArticals(responsedata.getArticles());
                    //setupRecyclerView((RecyclerView) recyclerView);
                    Log.d(TAG,"Response received is :- "+ responsedata.getArticles().toString());
                }

            }

            @Override
            public void onFailure(Call<JsonResponseObject> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }

    /**
     * Async task to insert data into database.
     * Room does not allow to do database operations on main thread
     */
    private static class InsertArticlesAsyncTask extends
            AsyncTask<List<Article>, Void, Void> {

        private ArticleDao asyncTaskDao;
        private NewsDataRepository delegate = null;

        InsertArticlesAsyncTask(ArticleDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Article>... params) {
             asyncTaskDao.insertAll(params[0]);
             return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    /**
     * Async task to insert data into database.
     * Room does not allow to do database operations on main thread
     */
    private static class GetCursorArticlesAsyncTask extends
            AsyncTask<Void, Void, Cursor> {

        private ArticleDao asyncTaskDao;
        private NewsDataRepository delegate = null;

        GetCursorArticlesAsyncTask(ArticleDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return asyncTaskDao.getCursorForArticles();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
        }
    }
}
