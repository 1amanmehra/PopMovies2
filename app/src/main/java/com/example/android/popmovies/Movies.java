package com.example.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

//Custom Movie class to store all the data returned by MOVIEDB http rquest.

public class Movies implements Parcelable {

    public String title;

    public String moviePoster;

    public String overview;

    public String voteAverage;

    public String releaseDate;


    public Movies(String movie_title, String moviePosterthumbnail, String movie_overview, String movie_voteAverage, String movie_releaseDate) {
        this.title = movie_title;
        this.moviePoster = moviePosterthumbnail;
        this.overview = movie_overview;
        this.voteAverage = movie_voteAverage;
        this.releaseDate = movie_releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.moviePoster);
        dest.writeString(this.overview);
        dest.writeString(this.voteAverage);
        dest.writeString(this.releaseDate);
    }

    protected Movies(Parcel in) {
        this.title = in.readString();
        this.moviePoster = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
