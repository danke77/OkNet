package com.danke.http.sample;

import android.app.Application;
import android.util.Log;

import com.danke.http.OkNet;
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

    @Override
    public void onCreate() {
        super.onCreate();

        OkNet.setBaseUrl("https://api.douban.com/v2/movie/");
        OkNet.setLoggingEnable(BuildConfig.DEBUG);
        OkNet.setMonitor(new IMonitor() {
            @Override
            public void monitor(@NotNull Request request, long tookMs, long contentLength) {
                Log.i(TAG, "request: " + new GsonBuilder().create().toJson(request)
                        + "\ntookMs: " + String.valueOf(tookMs)
                        + "\ncontentLength: " + String.valueOf(contentLength));
            }
        });
        OkNet.addInterceptor(new CommonHeaderInterceptor.Builder()
                .header("access_token_key", "access_token_value")
                .build());
    }
}
