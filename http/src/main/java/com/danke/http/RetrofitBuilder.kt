package com.danke.http

import com.danke.http.adapter.rxjava2.RxJava2ErrorHandlingCallAdapterFactory
import com.danke.http.converter.gson.GsonConverterFactory
import com.danke.http.monitor.IMonitor
import com.danke.http.monitor.MonitorInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * @author danke
 * @date 2018/6/28
 */
class RetrofitBuilder constructor(internal val baseUrl: String) {

    private var timeout: Long = 15

    private var clientBuilder: OkHttpClient.Builder? = null

    private var isLoggingEnable: Boolean = false

    private var monitor: IMonitor? = null

    private val converterFactories: ArrayList<Converter.Factory> = arrayListOf(GsonConverterFactory.create())

    private val callAdapterFactories: ArrayList<CallAdapter.Factory> = arrayListOf(RxJava2ErrorHandlingCallAdapterFactory.create())

    private val interceptors: ArrayList<Interceptor> = arrayListOf()

    fun timeout(timeout: Long) = apply { this.timeout = timeout }

    fun clientBuilder(clientBuilder: OkHttpClient.Builder?) = apply { this.clientBuilder = clientBuilder }

    fun isLoggingEnable(isLoggingEnable: Boolean) = apply { this.isLoggingEnable = isLoggingEnable }

    fun monitor(monitor: IMonitor?) = apply { this.monitor = monitor }

    fun converterFactories(vararg factory: Converter.Factory) = apply { this.converterFactories += factory }

    fun callAdapterFactories(vararg factory: CallAdapter.Factory) = apply { this.callAdapterFactories += factory }

    fun interceptors(vararg interceptor: Interceptor) = apply { this.interceptors += interceptor }

    fun build(): Retrofit {
        val builder = Retrofit.Builder()
                .client(createHttpClient())
                .baseUrl(baseUrl)

        converterFactories.forEach { builder.addConverterFactory(it) }
        callAdapterFactories.forEach { builder.addCallAdapterFactory(it) }

        return builder.build()
    }

    private fun createHttpClient(): OkHttpClient {
        val builder = clientBuilder ?: OkHttpClient.Builder()
        var isHttpLoggingInterceptorAdded = false

        interceptors.forEach {
            if (it is HttpLoggingInterceptor) {
                isHttpLoggingInterceptorAdded = true
            }
            builder.addInterceptor(it)
        }

        if (!isHttpLoggingInterceptorAdded) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(
                    if (isLoggingEnable) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
        }

        monitor?.let {
            builder.addInterceptor(MonitorInterceptor(it))
        }

        builder.connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)

        return builder.build()
    }
}