package com.udacity.moviestage2.ui.moviedetails;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.udacity.moviestage2.common.Constants;
import com.udacity.moviestage2.common.SingleLiveEvent;
import com.udacity.moviestage2.data.provider.api.movie.dto.MovieResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.TraillerListResponse;
import com.udacity.moviestage2.data.provider.api.movie.service.RetrofitApiUtils;
import com.udacity.moviestage2.data.provider.api.movie.service.RetrofitService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailsViewModel extends AndroidViewModel {

    private RetrofitService mRetrofitService;
    private final MutableLiveData<MovieResponse> mMovie = new MutableLiveData();
    private final MutableLiveData<TraillerListResponse> mTraillerListResponse = new MutableLiveData();
    private final SingleLiveEvent<Integer> mOpenMovieEvent = new SingleLiveEvent<>();

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        mRetrofitService = RetrofitApiUtils.getRetrofitService();
    }

    public void getMovieById(int id) {
        mRetrofitService.getMovieById(id, Constants.API_Key, "en-US")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError" + e);
                    }

                    @Override
                    public void onNext(MovieResponse movie) {
                        Timber.i("onNext ");
                        mMovie.postValue(movie);
                    }
                });
    }

    public void getTraillersMovieById(int id) {
        mRetrofitService.getVideosMovieById(id, Constants.API_Key, "en-US")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TraillerListResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError" + e);
                    }

                    @Override
                    public void onNext(TraillerListResponse traillerListResponse) {
                        Timber.i("onNext ");
                        mTraillerListResponse.postValue(traillerListResponse);
                    }
                });
    }

    public LiveData<MovieResponse> getMovie() {
        return mMovie;
    }

    public LiveData<TraillerListResponse> getTraillerList() {
        return mTraillerListResponse;
    }

    public MutableLiveData<Integer> getOpenMovieEvent() {
        return mOpenMovieEvent;
    }

}
