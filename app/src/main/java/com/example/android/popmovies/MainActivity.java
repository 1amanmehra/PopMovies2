package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<Movies>> {
    ProgressBar mProgressBar;
    String mSort;
    private MovieAdapter mAdapter;
    private RecyclerView mPosterList;
    ArrayList<Movies> moviesList = new ArrayList<>();
    String movieApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieApi = getResources().getString(R.string.movie_api);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSort = "popular";
        mPosterList = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mPosterList.setLayoutManager(layoutManager);
        mPosterList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(MainActivity.this, MainActivity.this);
        mAdapter.setData(moviesList);
        mPosterList.setAdapter(mAdapter);
        int loaderId = 100;
        Bundle bundle = new Bundle();
        bundle.putString("sortBy", "popular");
        /*
         * From MainActivity, we have implemented the LoaderCallbacks interface with the type of
         * String array. (implements LoaderCallbacks<String[]>) The variable callback is passed
         * to the call to initLoader below. This means that whenever the loaderManager has
         * something to notify us of, it will do so through this callback.
         */
        LoaderManager.LoaderCallbacks<ArrayList<Movies>> callback = MainActivity.this;

        /*
         * The second parameter of the initLoader method below is a Bundle. Optionally, you can
         * pass a Bundle to initLoader that you can then access from within the onCreateLoader
         * callback. In our case, we don't actually use the Bundle, but it's here in case we wanted
         * to.
         */

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(loaderId, bundle, callback);

    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, Bundle args) {
        final String sortBy = args.getString("sortBy");
        if (sortBy.equalsIgnoreCase("favourite")) {
            return new AsyncTaskLoader<ArrayList<Movies>>(this) {
                ArrayList<Movies> moviesList = null;

                @Override
                protected void onStartLoading() {
                    if (moviesList != null) {
                        deliverResult(moviesList);
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public ArrayList<Movies> loadInBackground() {
                    ArrayList<Movies> movies = new ArrayList<>();
                    String[] projection = new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID, MovieContract.MovieEntry.COLUMN_TITLE,
                            MovieContract.MovieEntry.COLUMN_POSTER,
                            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                            MovieContract.MovieEntry.COLUMN_OVERVIEW,
                            MovieContract.MovieEntry.COLUMN_RELEASE_DATE};
                    final Cursor cursor = getContentResolver().query(MovieContract.BASE_CONTENT_URI, projection, null, null, null);
                    if (cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {
                            Movies data = new Movies(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)), cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)), cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)), cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                            movies.add(data);
                        }
                    }
                    return movies;
                }

                public void deliverResult(ArrayList<Movies> data) {
                    moviesList = data;
                    super.deliverResult(data);
                }

            };

        } else {
            return new AsyncTaskLoader<ArrayList<Movies>>(this) {
                ArrayList<Movies> moviesList = null;

                @Override
                protected void onStartLoading() {
                    if (moviesList != null) {
                        deliverResult(moviesList);
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public ArrayList<Movies> loadInBackground() {
                    ArrayList<Movies> movies = new ArrayList<Movies>();
                    URL moviedbUrl = NetworkUtils.buildUrl(sortBy, movieApi);
                    String searchResults = null;
                    try {
                        searchResults = NetworkUtils.getResponseFromHttpUrl(moviedbUrl);
                        movies = getJsonData(searchResults);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return movies;
                }

                public void deliverResult(ArrayList<Movies> data) {
                    moviesList = data;
                    super.deliverResult(data);
                }

            };
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        if (data != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            moviesList = data;
            mAdapter.setData(moviesList);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    private void invalidateData() {
        mAdapter.setData(null);
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                if (mSort.equalsIgnoreCase("popular")) ;
                else
                    mSort = "popular";
                fetchData(mSort);

                Toast.makeText(this, "Popular is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.top_rated:
                // Ex: launching new activity/screen or show alert message
                if (mSort.equalsIgnoreCase("top_rated")) ;
                else
                    mSort = "top_rated";
                fetchData(mSort);
                Toast.makeText(this, "Top Rated is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.favourite:
                if (mSort.equalsIgnoreCase("favourite")) ;
                else
                    mSort = "favourite";
                fetchData(mSort);

                Toast.makeText(this, "Favourite is Selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void fetchData(String SortBy) {
        Bundle bundle = new Bundle();
        bundle.putString("sortBy", SortBy);
        invalidateData();
        getSupportLoaderManager().restartLoader(100, bundle, this);
    }

    protected ArrayList<Movies> getJsonData(String jsonData) {
        Movies movieDetail;
        ArrayList<Movies> arrayList = new ArrayList<>();
        if (jsonData != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonData);

                // Getting JSON Array node
                JSONArray results = jsonObj.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject first = results.getJSONObject(i);
                    String movieId = first.getString("id");
                    String posterPath = first.getString("poster_path");
                    String title = first.getString("title");
                    String overview = first.getString("overview");
                    String voteAverage = first.getString("vote_average");
                    String releaseDate = first.getString("release_date");
                    movieDetail = new Movies(movieId, title, posterPath, voteAverage, overview, releaseDate);
                    arrayList.add(movieDetail);
                }

            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
        return arrayList;

    }

    @Override
    public void onClick(int position) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Movies movie = moviesList.get(position);
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Content", movie);
        startActivity(intentToStartDetailActivity);
    }
}