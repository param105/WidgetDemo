package com.bruviti.widgetdemo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bruviti.widgetdemo.model.database.NewsDatabase;
import com.bruviti.widgetdemo.model.entity.Article;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    public NewsDatabase newsDatabase;
    public LiveData<List<Article>> articles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        newsDatabase = NewsDatabase.getDatabase(application);
        articles = newsDatabase.articleDao().getAllArticles();
    }


    public LiveData<List<Article>> getArticles(){
        return articles;
    }


}
