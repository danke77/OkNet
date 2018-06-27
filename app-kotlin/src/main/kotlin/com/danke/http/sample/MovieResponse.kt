package com.danke.http.sample

import android.support.annotation.Keep
import com.danke.http.response.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * @author danke
 * @date 2018/6/27
 */
@Keep
data class MovieResponse(
        // @SerializedName("count") val count: List<Int>,
        @SerializedName("title") val title: String?) : BaseResponse<String>()