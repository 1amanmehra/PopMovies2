package com.example.android.popmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


//Details of movie to be displayed by this detail activity.

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.ListItemClickListener, ReviewAdapter.ListItemClickListener {
    Movies movie;
    ArrayList<String> mVideoId;
    ArrayList<String> mImageId;
    private VideoAdapter mAdapter;
    private RecyclerView mVideoList;
    private RecyclerView mReviewList;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Movie Added to Favourites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addToDatabase();
            }
        });
        TextView mTitle = (TextView) findViewById(R.id.title);
        ImageView mPoster = (ImageView) findViewById(R.id.poster);
        TextView mVote = (TextView) findViewById(R.id.vote_average);
        TextView mDate = (TextView) findViewById(R.id.release_date);
        TextView mOverview = (TextView) findViewById(R.id.overview);
        String imgAddress;
        Intent intentThatStartedThisActivity = getIntent();
        String videos = null;
        String review = null;
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("Content")) {
                movie = intentThatStartedThisActivity.getParcelableExtra("Content");
                mTitle.setText(movie.getTitle());
                mVote.setText(movie.getVoteAverage());
                mDate.setText(movie.getReleaseDate());
                mOverview.setText(movie.getOverview());
                imgAddress = movie.getMoviePoster();
                URL videoUrl = NetworkUtils.buildUrl(movie.getMovieId() + "/videos", getResources().getString(R.string.movie_api));
                URL imageUrl = NetworkUtils.buildUrl(movie.getMovieId() + "/reviews", getResources().getString(R.string.movie_api));
                new NetworkQueryTask().execute(videoUrl, imageUrl);
                URL newUrl = NetworkUtils.buildImageUrl(imgAddress);
                Picasso.with(this).load(newUrl.toString()).into(mPoster);
                mVideoList = (RecyclerView) findViewById(R.id.recycler_view_videos);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                mVideoList.setLayoutManager(layoutManager);
                mVideoList.setHasFixedSize(true);
                mAdapter = new VideoAdapter(this, this);
                mReviewList = (RecyclerView) findViewById(R.id.recycler_view_review);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
                mReviewList.setLayoutManager(layoutManager1);
                mReviewList.setHasFixedSize(true);
                mReviewAdapter = new ReviewAdapter(this, this);
            }
        }
    }

    public void addToDatabase() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getMoviePoster());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        getContentResolver().insert(MovieContract.BASE_CONTENT_URI, values);
    }

    @Override
    public void onClick(int itemclickIndex) {
        String video = mVideoId.get(itemclickIndex);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public class NetworkQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... params) {
            URL searchUrl = params[0];
            URL imgUrl = params[1];
            String[] result = new String[]{null, null};
            String searchResults = null;
            String imgSearchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                imgSearchResults = NetworkUtils.getResponseFromHttpUrl(imgUrl);
                result[0] = searchResults;
                result[1] = imgSearchResults;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] results) {
            String searchResults = results[0];
            String imgSearchResults = results[1];
            if (searchResults != null && !searchResults.equals("")) {
                mVideoId = getVideoJsonData(searchResults);
                mAdapter.setData(mVideoId);
                mVideoList.setAdapter(mAdapter);
            }
            if (imgSearchResults != null && !imgSearchResults.equals("")) {
                ArrayList<String> mId = getReviewJsonData(imgSearchResults);
                mReviewAdapter.setData(mId);
                mReviewList.setAdapter(mReviewAdapter);
            }
        }
    }

    protected ArrayList<String> getVideoJsonData(String jsonData) {
        ArrayList<String> video = new ArrayList<>();
        if (jsonData != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonData);

                // Getting JSON Array node
                JSONArray results = jsonObj.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject first = results.getJSONObject(i);
                    String mVideoId = first.getString("key");
                    video.add(mVideoId);
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
        return video;

    }

    protected ArrayList<String> getReviewJsonData(String jsonData) {
        ArrayList<String> review = new ArrayList<>();
        if (jsonData != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonData);

                // Getting JSON Array node
                JSONArray results = jsonObj.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject first = results.getJSONObject(i);
                    String mReviewId = first.getString("content");
                    review.add(mReviewId);
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
        return review;

    }
}

