package com.bruviti.widgetdemo.model;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bruviti.widgetdemo.model.database.NewsDatabase;
import com.bruviti.widgetdemo.model.entity.Article;
import com.bruviti.widgetdemo.model.roomdao.ArticleDao;


/**
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class NewsAppContentProvider extends ContentProvider {

    public static final String TAG ="NewsAppContentProvider";

    /** The match code for some items in the News table. */
    private static final int CODE_NEWS_ALL = 1;

    /** The match code for an item in the News table. */
    private static final int CODE_NEWS_ITEM = 2;

    /** Content providing URI and constatnts */
    public static final String SCHEMA = "content://";
    public static final String AUTHORITY = "com.bruviti.widgetdemo.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEMA + AUTHORITY);


    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "", CODE_NEWS_ALL);
        MATCHER.addURI(AUTHORITY, "/*", CODE_NEWS_ALL);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: Uri -"+ uri);
        final int code = MATCHER.match(uri);
        if (code == CODE_NEWS_ALL || code == CODE_NEWS_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            ArticleDao articleDao = NewsDatabase.getDatabase(context).articleDao();
            Cursor cursor = null;
            cursor = articleDao.getCursorForArticles();
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "query: Uri -"+ uri);
        switch (MATCHER.match(uri)) {

            case CODE_NEWS_ALL:
                return BASE_CONTENT_URI + "." + Article.TABLE_NAME;
            case CODE_NEWS_ITEM:
                return BASE_CONTENT_URI + "." + Article.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}