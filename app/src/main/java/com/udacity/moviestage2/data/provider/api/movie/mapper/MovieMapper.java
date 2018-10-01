package com.udacity.moviestage2.data.provider.api.movie.mapper;


import com.udacity.moviestage2.data.database.MovieEntry;
import com.udacity.moviestage2.data.database.ReviewEntry;
import com.udacity.moviestage2.data.model.MovieModel;
import com.udacity.moviestage2.data.model.ReviewModel;
import com.udacity.moviestage2.data.model.TraillerModel;
import com.udacity.moviestage2.data.provider.api.movie.dto.MovieResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.ReviewListResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.ReviewResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.TraillerListResponse;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {

    public List<MovieModel> convertListResponseToListModel(List<MovieResponse> movieListResponse) {
        ModelMapper modelMapper = new ModelMapper();
        List<MovieModel> movieModelList = new ArrayList<>();
        MovieModel movieModel;
        for(int i = 0; i < movieListResponse.size() ; i++){
            movieModel = modelMapper.map(movieListResponse.get(i), MovieModel.class);
            movieModelList.add(movieModel);
        }

        return movieModelList;
    }

    public MovieModel convertResponseToModel(MovieResponse movieResponse) {
        ModelMapper modelMapper = new ModelMapper();
        MovieModel movieModel = modelMapper.map(movieResponse, MovieModel.class);
        return movieModel;
    }

    public List<MovieModel> convertEntryListToModels(List<MovieEntry> movieEntryList) {
        ModelMapper modelMapper = new ModelMapper();
        List<MovieModel> movieModelList = new ArrayList<>();
        MovieModel movieModel;

        for(int i = 0 ; i < movieEntryList.size() ; i++){
            movieModel = modelMapper.map(movieEntryList.get(i), MovieModel.class);
            movieModelList.add(movieModel);
        }
        return movieModelList;
    }

    public MovieModel convertEntryToModel(MovieEntry movieEntry) {
        ModelMapper modelMapper = new ModelMapper();
        MovieModel movieModel;
        ReviewModel reviewModel;
        List<ReviewEntry> reviewEntryList;
        List<ReviewModel> reviewModelList = new ArrayList<>();

        movieModel = modelMapper.map(movieEntry, MovieModel.class);

        if(movieEntry.getReviewEntryList() != null){
            reviewEntryList = movieEntry.getReviewEntryList();
            for (int i=0 ; i < reviewEntryList.size() ; i++){
                reviewModel = modelMapper.map(reviewEntryList.get(i), ReviewModel.class);
                reviewModelList.add(reviewModel);
            }
            movieModel.setReviewModelList(reviewModelList);
        }


        return movieModel;
    }

    public List<TraillerModel> convertListTraillerResponseToModels(TraillerListResponse traillerListResponse) {
        ModelMapper modelMapper = new ModelMapper();
        List<TraillerModel> traillerModelList = new ArrayList<>();
        TraillerModel traillerModel;
        for(int i = 0 ; i < traillerListResponse.getResults().size() ; i++){
            traillerModel = modelMapper.map(traillerListResponse.getResults().get(i), TraillerModel.class);
            traillerModelList.add(traillerModel);
        }
        return traillerModelList;
    }

    public List<ReviewModel> convertListReviewResponseToModels(ReviewListResponse reviewListResponse) {
        ModelMapper modelMapper = new ModelMapper();
        List<ReviewModel> reviewModelList = new ArrayList<>();
        ReviewModel reviewModel;
        for(int i = 0 ; i < reviewListResponse.getResults().size() ; i++){
            reviewModel = modelMapper.map(reviewListResponse.getResults().get(i), ReviewModel.class);
            reviewModelList.add(reviewModel);
        }
        return reviewModelList;
    }

    public List<ReviewEntry> convertListReviewResponseToEntry(ReviewListResponse reviewListResponse) {
        ModelMapper modelMapper = new ModelMapper();
        List<ReviewEntry> reviewEntryList = new ArrayList<>();
        ReviewEntry reviewEntry = new ReviewEntry();
        for(int i = 0 ; i < reviewListResponse.getResults().size() ; i++){
            reviewEntry = modelMapper.map(reviewListResponse.getResults().get(i), ReviewEntry.class);
            reviewEntryList.add(reviewEntry);
        }
        return reviewEntryList;
    }

    public List<ReviewEntry> convertListReviewModelToReviewerEntryList(List<ReviewModel> reviewModelList){
        ModelMapper modelMapper = new ModelMapper();
        List<ReviewEntry> reviewEntryList = new ArrayList<>();
        ReviewEntry reviewEntry;
        for(int i = 0 ; i < reviewModelList.size() ; i++){
            reviewEntry = modelMapper.map(reviewModelList.get(i), ReviewEntry.class);
            reviewEntryList.add(reviewEntry);
        }
        return reviewEntryList;

    }

    public ReviewModel convertReviewResponseToModel(ReviewResponse reviewResponse) {
        ModelMapper modelMapper = new ModelMapper();
        ReviewModel reviewModel = modelMapper.map(reviewResponse, ReviewModel.class);
        return reviewModel;
    }

}

