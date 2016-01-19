package com.hisham.mahmoud.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;


@SimpleSQLTable(table = "favoriate_movie", provider = "MovieProvider")
public class Movie implements Parcelable {
    @SimpleSQLColumn(value = "_id", primary = true)
    private int _id;
    @SimpleSQLColumn(value = "movie_id")
    private String movieId;
    @SimpleSQLColumn(value = "movie_title")
    private String title;
    @SimpleSQLColumn(value = "movie_date")
    private String releaseDate;
    @SimpleSQLColumn(value = "movie_vote")
    private String voteAverage;
    @SimpleSQLColumn(value = "movie_overview")
    private String overview;
    @SimpleSQLColumn(value = "movie_poster")
    private String posterPath;
    @SimpleSQLColumn(value = "fav")
    private int isFave;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getIsFave() {
        return isFave;
    }

    public void setIsFave(int isFave) {
        this.isFave = isFave;
    }

    private List<Trailer> trailers;
  private List<Review> reviews;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Movie() {
    }

    public Movie(String movieId, String title, String releaseDate, String voteAverage, String overview, String posterPath , int isFave) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.isFave=isFave;
    }

    public Movie(String movieId, String title, String posterPath , int isFave) {
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.isFave=isFave;
    }

    protected Movie(Parcel in) {
        movieId = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        isFave=in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeInt(isFave);
    }

    private void readFromParcel(Parcel in) {


        movieId = in.readString();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        isFave= in.readInt();
    }


}