package com.bruviti.widgetdemo.model.database;


import android.content.Context;

import androidx.room.Room;

import com.bruviti.widgetdemo.util.Globals;

public class DatabaseClient {

    private Context mContext;
    private static DatabaseClient mInstance;

    //our app database object
    private NewsDatabase newsDatabase;

    private DatabaseClient(Context mContext) {
        this.mContext = mContext;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        newsDatabase = Room.databaseBuilder(mContext, NewsDatabase.class, Globals.TABLE_NAME).build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public NewsDatabase getDatabase() {
        return newsDatabase;
    }
}