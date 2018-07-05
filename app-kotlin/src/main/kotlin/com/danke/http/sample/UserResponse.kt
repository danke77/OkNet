package com.danke.http.sample

import android.support.annotation.Keep
import com.danke.http.response.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * @author danke
 * @date 2018/7/5
 */
@Keep
data class UserResponse(
        // @SerializedName("type") val type: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("id") val id: Long,
        @SerializedName("avatar_url") val avatar: String?,
        @SerializedName("html_url") val url: String) : BaseResponse<String>()