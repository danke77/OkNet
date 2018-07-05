package com.danke.http.sample

import android.app.Application
import android.util.Log
import com.danke.http.OkNet
import com.danke.http.RetrofitBuilder
import com.danke.http.interceptor.CommonHeaderInterceptor
import com.danke.http.monitor.IMonitor
import com.danke.http.util.getDefaultUserAgent
import com.google.gson.GsonBuilder
import okhttp3.Request

/**
 * @author danke
 * @date 2018/6/27
 */
class App : Application() {

    companion object {
        private const val TAG = "OkNet"
        private const val BASE_URL = "https://api.github.com/"
    }

    override fun onCreate() {
        super.onCreate()

        OkNet.apply {
            baseUrl = BASE_URL
            retrofitBuilder = RetrofitBuilder(baseUrl = BASE_URL)
                    .isLoggingEnable(BuildConfig.DEBUG)
                    .monitor(object : IMonitor {
                        override fun monitor(request: Request, tookMs: Long, contentLength: Long) {
                            Log.i(TAG, "request: " + GsonBuilder().create().toJson(request)
                                    + "\ntookMs: " + tookMs.toString()
                                    + "\ncontentLength: " + contentLength.toString())
                        }
                    })
                    .interceptors(CommonHeaderInterceptor.Builder()
                            .header("access_token_key", "access_token_value")
                            .header("User-Agent", getDefaultUserAgent(applicationContext))
                            .build())
        }
    }
}
