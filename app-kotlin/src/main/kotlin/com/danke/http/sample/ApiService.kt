package com.danke.http.sample

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * @author danke
 * @date 2018/6/27
 */
interface ApiService {
    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Observable<Response<UserResponse>>
}