package com.danke.http.sample;

import android.support.annotation.Keep;

import com.danke.http.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * @author danke
 * @date 2018/6/27
 */
@Keep
public class MovieResponse extends BaseResponse<String> {

    // @SerializedName("count")
    // public List<Integer> count;

    @SerializedName("title")
    public String title;
}