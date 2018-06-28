package com.danke.http.sample;

import android.app.Application;
import android.util.Log;

import com.danke.http.OkNet;
import com.danke.http.RetrofitBuilder;
import com.danke.http.interceptor.CommonHeaderInterceptor;
import com.danke.http.monitor.IMonitor;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import okhttp3.Request;

/**
 * @author danke
 * @date 2018/6/27
 */
public class App extends Application {

    private static final String TAG = "OkNet";
    private static final String BASE_URL = "https://api.douban.com/v2/movie/";

    @Override
    public void onCreate() {
        super.onCreate();

        OkNet.setBaseUrl(BASE_URL);
        OkNet.setRetrofitBuilder(new RetrofitBuilder(BASE_URL)
                .isLoggingEnable(BuildConfig.DEBUG)
                .monitor((request, tookMs, contentLength) ->
                        Log.i(TAG, "request: " + new GsonBuilder().create().toJson(request)
                                + "\ntookMs: " + String.valueOf(tookMs)
                                + "\ncontentLength: " + String.valueOf(contentLength)))
                .interceptors(new CommonHeaderInterceptor.Builder()
                        .header("access_token_key", "access_token_value")
                        .build()));
    }
}
