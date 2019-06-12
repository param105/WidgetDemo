package com.bruviti.widgetdemo.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.roomdao.ArticleDao;
import com.bruviti.widgetdemo.util.Globals;

@Database(entities = {Article.class},version = 1,exportSchema = true)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();

    private static NewsDatabase INSTANCE;

    public static NewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    NewsDatabase.class,
                                    Globals.TABLE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }


}

