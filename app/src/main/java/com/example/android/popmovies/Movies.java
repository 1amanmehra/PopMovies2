package com.example.android.popmovies;

import java.io.Serializable;

//Custom Movie class to store all the data returned by MOVIEDB http rquest.

@SuppressWarnings("serial")
public class Movies implements Serializable {

    public String title;

    public String moviePoster;

    public String overview;

    public String voteAverage;

    public String releaseDate;


    public Movies(String movie_title,String moviePosterthumbnail,String movie_overview,String movie_voteAverage,String movie_releaseDate)
    {
        this.title=movie_title;
        this.moviePoster=moviePosterthumbnail;
        this.overview=movie_overview;
        this.voteAverage=movie_voteAverage;
        this.releaseDate=movie_releaseDate;
    }

    public String getTitle()
    {
        return title;
    }
    public String getMoviePoster()
    {
        return moviePoster;
    }
    public String getOverview()
    {
        return overview;
    }
    public String getVoteAverage()
    {
        return voteAverage;
    }
    public String getReleaseDate()
    {
        return releaseDate;
    }
}
