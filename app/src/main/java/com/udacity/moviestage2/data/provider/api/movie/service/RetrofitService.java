package com.udacity.moviestage2.data.provider.api.movie.service;



import com.udacity.moviestage2.common.Constants;
import com.udacity.moviestage2.data.provider.api.movie.dto.Example;
import com.udacity.moviestage2.data.provider.api.movie.dto.MovieResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.ReviewListResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.TraillerListResponse;
import com.udacity.moviestage2.data.provider.api.movie.mapper.MovieMapper;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitService {

    MovieMapper movieMapper = new MovieMapper();

    @GET(Constants.URL_POPULAR_MOVIES)
    Observable<Example> getPopularMovies(@Query("api_key") String api_key,
                                                        @Query("language") String language,
                                                        @Query("page") String page);

    @GET(Constants.URL_TOP_RATED)
    Observable<Example> getTopRatedMovies(@Query("api_key") String api_key,
                                                           @Query("language") String language,
                                                           @Query("page") String page);

    @GET(Constants.URL_MOVIE_BY_ID+"{movie_id}")
    Observable<MovieResponse> getMovieById(@Path("movie_id") int id,
                                           @Query("api_key") String api_key,
                                           @Query("language") String language);

    @GET(Constants.URL_MOVIE_BY_ID+"{movie_id}/videos")
    Observable<TraillerListResponse> getVideosMovieById(@Path("movie_id") int id,
                                                        @Query("api_key") String api_key,
                                                        @Query("language") String language);

    @GET(Constants.URL_MOVIE_BY_ID+"{movie_id}/reviews")
    Observable<ReviewListResponse> getReviewsById(@Path("movie_id") int id,
                                                  @Query("api_key") String api_key,
                                                  @Query("language") String language);




}