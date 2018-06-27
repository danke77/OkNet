package com.danke.http.sample;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.*;

/**
 * @author danke
 * @date 2018/6/27
 */
public interface ApiService {

    @GET("top250")
    Observable<Response<MovieResponse>> getTop250(@Query("start") int start,
                                                  @Query("count") int count);

    @FormUrlEncoded
    @POST("top250")
    Observable<Response<MovieResponse>> postTop250(@Field("start") int start,
                                                   @Field("count") int count);

    @GET("subject/{id}")
    Observable<Response<MovieResponse>> getSubject(@Path("id") long id);
}