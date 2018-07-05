package com.danke.http.sample;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.*;

/**
 * @author danke
 * @date 2018/6/27
 */
public interface ApiService {

    @GET("users/{user}")
    Observable<Response<UserResponse>> getUser(@Path("user") String user);
}