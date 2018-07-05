package com.danke.http.sample;

import android.support.annotation.Keep;

import com.danke.http.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * @author danke
 * @date 2018/6/27
 */
@Keep
public class UserResponse extends BaseResponse<String> {

    // @SerializedName("type")
    // int type;

    @SerializedName("name")
    String name;

    @SerializedName("id")
    long id;

    @SerializedName("avatar_url")
    String avatar;

    @SerializedName("html_url")
    String url;
}