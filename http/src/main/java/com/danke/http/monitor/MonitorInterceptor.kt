package com.danke.http.monitor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author danke
 * @date 2018/6/25
 */
class MonitorInterceptor constructor(private val monitor: IMonitor?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val startNs = System.nanoTime()
        val request = chain.request()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()
        val contentLength = responseBody?.contentLength()

        monitor?.monitor(request, tookMs, contentLength ?: 0)

        return response
    }
}