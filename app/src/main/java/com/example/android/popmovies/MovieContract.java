package com.example.android.popmovies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aman on 17/7/17.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "movies";

    public class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "moviePoster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";

    }
}