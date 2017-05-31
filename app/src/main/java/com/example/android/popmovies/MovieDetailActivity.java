package com.example.android.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

//Details of movie to be displayed by this detail activity.

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        TextView mTitle = (TextView) findViewById(R.id.title);
        ImageView mPoster    = (ImageView) findViewById(R.id.poster);
        TextView mVote = (TextView) findViewById(R.id.vote_average);
        TextView mDate = (TextView) findViewById(R.id.release_date);
        TextView mOverview = (TextView) findViewById(R.id.overview);
        String imgAddress;

        Intent intentThatStartedThisActivity = getIntent();


        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("title")) {
                mTitle.setText(intentThatStartedThisActivity.getStringExtra("title"));
                mVote.setText(intentThatStartedThisActivity.getStringExtra("voteAverage"));
                        mDate.setText(intentThatStartedThisActivity.getStringExtra("releaseDate"));
                mOverview.setText(intentThatStartedThisActivity.getStringExtra("overview"));
                        imgAddress=intentThatStartedThisActivity.getStringExtra("moviePoster");
                URL newUrl =NetworkUtils.buildImageUrl(imgAddress);
                Picasso.with(this).load(newUrl.toString()).into(mPoster);
            }
        }
    }
    }
