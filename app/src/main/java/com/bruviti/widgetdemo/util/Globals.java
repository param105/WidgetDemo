package com.bruviti.widgetdemo.util;

import android.net.Uri;
import android.provider.BaseColumns;

public class Globals implements BaseColumns {

    public static final String TABLE_NAME = "NEWS";
    public static final String COL_NEWS_TITLE = "news_title";
    public static final String SCHEMA = "content://";
    public static final String AUTHORITY = "com.bruviti.widgetdemo";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEMA + AUTHORITY);
    public static final String PATH_NEWS = "news";
    public static final Uri PATH_NEWS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

}
