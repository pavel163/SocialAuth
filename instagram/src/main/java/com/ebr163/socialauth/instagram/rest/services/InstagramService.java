package com.ebr163.socialauth.instagram.rest.services;

import com.ebr163.socialauth.instagram.model.InstagramProfile;
import com.ebr163.socialauth.instagram.model.InstagramResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InstagramService {

    @GET("users/self")
    Call<InstagramResponse<InstagramProfile>> getProfile(
            @Query("access_token") String token);

}
