package com.example.android.popmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aman on 17/7/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "movies";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
