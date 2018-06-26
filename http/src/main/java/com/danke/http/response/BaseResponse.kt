package com.danke.http.response

import com.google.gson.annotations.SerializedName

/**
 * @author danke
 * @date 2018/6/19
 */
open class BaseResponse<out T> {

    @SerializedName("code")
    val code: Int? = -1

    @SerializedName("msg")
    val msg: String? = null

    @SerializedName("data")
    val data: T? = null
}