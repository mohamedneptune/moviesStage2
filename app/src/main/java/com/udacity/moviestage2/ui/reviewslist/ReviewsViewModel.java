package com.udacity.moviestage2.ui.reviewslist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.udacity.moviestage2.common.Constants;
import com.udacity.moviestage2.data.model.ReviewModel;
import com.udacity.moviestage2.data.provider.api.movie.dto.ReviewListResponse;
import com.udacity.moviestage2.data.provider.api.movie.service.RetrofitApiUtils;
import com.udacity.moviestage2.data.provider.api.movie.service.RetrofitService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class ReviewsViewModel extends AndroidViewModel {

    private RetrofitService mRetrofitService;
    private final MutableLiveData<ReviewListResponse> mReviewListResponse = new MutableLiveData();
    private List<ReviewModel> reviewModelList;


    public ReviewsViewModel(@NonNull Application application) {
        super(application);
        mRetrofitService = RetrofitApiUtils.getRetrofitService();
    }

    public void getReviewsMovieById(int id) {
        mRetrofitService.getReviewsById(id, Constants.API_Key, "en-US")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewListResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError" + e);
                    }

                    @Override
                    public void onNext(ReviewListResponse reviewListResponse) {
                        Timber.i("onNext ");
                        mReviewListResponse.postValue(reviewListResponse);
                    }
                });
    }

    public LiveData<ReviewListResponse> getReviewList() {
        return mReviewListResponse;
    }

    public List<ReviewModel> getReviewModelList() {
        return reviewModelList;
    }

    public void setReviewModelList(List<ReviewModel> reviewModelList) {
        this.reviewModelList = reviewModelList;
    }
}