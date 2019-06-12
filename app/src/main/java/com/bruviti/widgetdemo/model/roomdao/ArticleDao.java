package com.bruviti.widgetdemo.model.roomdao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bruviti.widgetdemo.model.entity.Article;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ArticleDao {

    @Query("Select * from ARTICLES")
    LiveData<List<Article>> getAllArticles();

    @Insert
    void insert(Article article);

    @Insert
    void insertAll(List<Article> articles);
}
