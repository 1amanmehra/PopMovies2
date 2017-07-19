package com.example.android.popmovies;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class NetworkUtils {
// ENTER YOUR MOVIEDB API_KEY IN THE STRING BELOW.

    final static String MOVIE_SCHEME = "http";

    final static String MOVIE_BASE_URL = "api.themoviedb.org";

    final static String MOVIE_PATH = "3/movie";

    final static String IMAGE_SCHEME = "http";

    final static String IMAGE_BASE_URL = "image.tmdb.org";

    final static String IMAGE_PATH = "t/p/w185";


    //This method builds the MovieDB URL for the HTTP request.


    public static URL buildUrl(String movieSort, String MOVIE_API_QUERY) {
        Uri.Builder builtUri = new Uri.Builder();
        builtUri.scheme(MOVIE_SCHEME)
                .authority(MOVIE_BASE_URL)
                .appendEncodedPath(MOVIE_PATH)
                .appendEncodedPath(movieSort)
                .appendQueryParameter("api_key", MOVIE_API_QUERY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    //This method builds the image URL for the image.

    public static URL buildImageUrl(String imagePath) {
        Uri.Builder builtUri = new Uri.Builder();
        builtUri.scheme(IMAGE_SCHEME)
                .authority(IMAGE_BASE_URL)
                .appendEncodedPath(IMAGE_PATH)
                .appendEncodedPath(imagePath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //This method returns the entire result from the HTTP response.

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            // given a url open a connection
            URLConnection c = url.openConnection();

            // set the connection timeout to 5 seconds and the read timeout to 10 seconds
            c.setConnectTimeout(5000);
            c.setReadTimeout(10000);

            // get a stream to read data from
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));

            // use the stream...

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");


            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}