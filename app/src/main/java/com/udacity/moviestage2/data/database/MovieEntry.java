package com.udacity.moviestage2.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey
    private int id;
    private String posterPath;
    private Boolean adult;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private Double popularity;
    private int voteCount;
    private Boolean video;
    private Double voteAverage;
    @TypeConverters(DataConverter.class)
    private List<ReviewEntry> reviewEntryList;

    @Ignore
    public MovieEntry(String posterPath, Boolean adult, String overview, String releaseDate,
                      String originalTitle, String originalLanguage, String title,
                      String backdropPath, Double popularity, Integer voteCount, Boolean video,
                      Double voteAverage, List<ReviewEntry> reviewEntryList) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.reviewEntryList = reviewEntryList;
    }

    public MovieEntry(int id, String posterPath, String overview, String releaseDate,
                      String originalTitle, String backdropPath, Integer voteCount, Boolean video,
                      Double voteAverage, List<ReviewEntry> reviewEntryList) {
        this.id = id;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.reviewEntryList = reviewEntryList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public List<ReviewEntry> getReviewEntryList() {
        return reviewEntryList;
    }

    public void setReviewEntryList(List<ReviewEntry> reviewEntryList) {
        this.reviewEntryList = reviewEntryList;
    }
}

