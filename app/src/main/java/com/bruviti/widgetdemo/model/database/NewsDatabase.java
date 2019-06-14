package com.bruviti.widgetdemo.model.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.roomdao.ArticleDao;
import com.bruviti.widgetdemo.util.Globals;

@Database(entities = {Article.class},version = 1,exportSchema = true)
public abstract class NewsDatabase extends RoomDatabase {
    public static String TAG ="NewsDatabase";
    public static final String DB_NAME = "newsdb.sqlite";

    public abstract ArticleDao articleDao();
    private static NewsDatabase INSTANCE;

    public static NewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    NewsDatabase.class,
                                    DB_NAME)
                                    .addCallback(new Callback() {
                                        @Override
                                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                            super.onCreate(db);
                                            Log.d(TAG,"database created");

                                        }

                                        @Override
                                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                            super.onOpen(db);
                                            Log.d(TAG,"database Opened");
                                        }
                                    })
                                    .build();
                }
            }
        }
        return INSTANCE;
    }




}

