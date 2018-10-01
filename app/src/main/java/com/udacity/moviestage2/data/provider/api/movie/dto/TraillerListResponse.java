package com.udacity.moviestage2.data.provider.api.movie.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TraillerListResponse {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TraillerResponse> results = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TraillerResponse> getResults() {
        return results;
    }

    public void setResults(List<TraillerResponse> results) {
        this.results = results;
    }

}
