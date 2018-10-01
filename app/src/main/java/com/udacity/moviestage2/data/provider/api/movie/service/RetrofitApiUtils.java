package com.udacity.moviestage2.data.provider.api.movie.service;


import com.udacity.moviestage2.common.Constants;

public class RetrofitApiUtils {

    public static RetrofitService getRetrofitService() {
        return RetrofitClient.getClient(Constants.BASE_SERVER).create(RetrofitService.class);
    }

}
