package com.bruviti.widgetdemo.model.roomdao;

import android.database.Cursor;

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

    @Query("Select * from articles")
    LiveData<List<Article>> getAllArticles();

    @Insert
    void insert(Article article);

    @Insert
    void insertAll(List<Article> articles);

    @Query("SELECT * FROM " + Article.TABLE_NAME)
     Cursor getCursorForArticles();


    @Query("SELECT * FROM " + Article.TABLE_NAME + " WHERE " + Article.COLUMN_ID + " = :id")
    Cursor selectArticleById(long id);

}
