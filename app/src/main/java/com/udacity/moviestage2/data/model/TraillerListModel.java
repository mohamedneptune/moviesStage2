package com.udacity.moviestage2.data.model;

import com.udacity.moviestage2.data.provider.api.movie.dto.MovieResponse;

import java.util.List;

public class TraillerListModel {

    private Integer id;

    private List<TraillerModel> results = null;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TraillerModel> getResults() {
        return results;
    }

    public void setResults(List<TraillerModel> results) {
        this.results = results;
    }

}
