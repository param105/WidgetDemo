package com.bruviti.widgetdemo.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.bruviti.widgetdemo.model.database.NewsDatabase;
import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.roomdao.ArticleDao;

import java.util.List;

public class NewsDataRepository {


    public NewsDatabase newsDatabase;
    public ArticleDao articleDao;
    private MutableLiveData<List<Article>> articals = new MutableLiveData<>();


    NewsDataRepository(Context context){
        newsDatabase = NewsDatabase.getDatabase(context);
        articleDao = newsDatabase.articleDao();
    }

    public MutableLiveData<List<Article>> getAllArticles(){
        return articleDao.getAllArticles();
    }

    public void insertArticals(List<Article> articles){
         new InsertArticlesAsyncTask(articleDao).execute(articles);
    }



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
}
