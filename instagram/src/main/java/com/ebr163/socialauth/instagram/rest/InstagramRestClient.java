package com.ebr163.socialauth.instagram.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstagramRestClient {

    private static final String API_URL = "https://api.instagram.com/v1/";
    private Retrofit retrofit;
    private Object service;

    public InstagramRestClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T getService(Class<T> serviceClass) {
        if (service == null || !serviceClass.isInstance(service))
            service = retrofit.create(serviceClass);
        return (T) service;
    }
}