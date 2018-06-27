package com.danke.http.sample

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * @author danke
 * @date 2018/6/27
 */
interface ApiService {

    @GET("top250")
    fun getTop250(@Query("start") start: Int = 0,
                  @Query("count") count: Int = 10): Observable<Response<MovieResponse>>

    @FormUrlEncoded
    @POST("top250")
    fun postTop250(@Field("start") start: Int = 0,
                   @Field("count") count: Int = 10): Observable<Response<MovieResponse>>

    @GET("subject/{id}")
    fun getSubject(@Path("id") id: Long): Observable<Response<MovieResponse>>
}