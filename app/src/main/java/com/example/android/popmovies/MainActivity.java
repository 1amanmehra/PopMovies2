package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{
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
        fetchData(mSort);
        mPosterList = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mPosterList.setLayoutManager(layoutManager);
        mPosterList.setHasFixedSize(true);

    }


    //Async task to get data from HTTP request in background.

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String SearchResults = null;
            try {
                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SearchResults;
        }

        @Override
        protected void onPostExecute(String SearchResults) {
            if (SearchResults != null && !SearchResults.equals("")) {
                mProgressBar.setVisibility(View.INVISIBLE);
                getJsonData(SearchResults);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void fetchData(String SortBy) {
        URL moviedbUrl = NetworkUtils.buildUrl(SortBy,movieApi);
        new MovieQueryTask().execute(moviedbUrl);
    }

    protected void getJsonData(String jsonData) {
        Movies movieDetail;
        ArrayList<Movies> arrayList =new ArrayList<>();
        if (jsonData != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonData);

                // Getting JSON Array node
                JSONArray results = jsonObj.getJSONArray("results");
                // looping through All Contacts
                for (int i = 0; i < results.length(); i++) {
                    JSONObject first = results.getJSONObject(i);

                    String posterPath = first.getString("poster_path");
                    String title = first.getString("title");
                    String overview = first.getString("overview");
                    String voteAverage = first.getString("vote_average");
                    String releaseDate = first.getString("release_date");
                    movieDetail= new Movies(title,posterPath,overview,voteAverage,releaseDate);
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
        moviesList=arrayList;
        mAdapter = new MovieAdapter(moviesList, this, this);
        mPosterList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(int position) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Movies movie=moviesList.get(position);
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
       Bundle extras = new Bundle();
        extras.putString("title",movie.getTitle());
        extras.putString("moviePoster",movie.getMoviePoster());
        extras.putString("overview",movie.getOverview());
        extras.putString("voteAverage",movie.getVoteAverage());
        extras.putString("releaseDate",movie.getReleaseDate());
        intentToStartDetailActivity.putExtras(extras);
        startActivity(intentToStartDetailActivity);
    }
}
