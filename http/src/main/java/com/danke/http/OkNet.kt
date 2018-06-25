package com.danke.http

import com.danke.http.converter.gson.GsonConverterFactory
import com.danke.http.monitor.IMonitor
import com.danke.http.monitor.MonitorInterceptor
import com.danke.http.util.readWriteLazy
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * @author danke
 * @date 2018/6/19
 */
object OkNet {

    @JvmStatic
    var baseUrl: String = ""

    @JvmStatic
    var timeout: Long = 15

    @JvmStatic
    var clientBuilder: OkHttpClient.Builder? = null

    @JvmStatic
    var isLoggingEnable: Boolean = false

    @JvmStatic
    var monitor: IMonitor? = null

    private val converterFactories: ArrayList<Converter.Factory> = arrayListOf(GsonConverterFactory.create())

    private val callAdapterFactories: ArrayList<CallAdapter.Factory> = arrayListOf(RxJava2CallAdapterFactory.create())

    private val interceptors: ArrayList<Interceptor> = arrayListOf()

    private val retrofit: Retrofit by readWriteLazy { createRetrofit() }

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

    private fun createRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
                .client(createHttpClient())
                .baseUrl(baseUrl)

        converterFactories.forEach { builder.addConverterFactory(it) }
        callAdapterFactories.forEach { builder.addCallAdapterFactory(it) }

        return builder.build()
    }

    @JvmStatic
    fun addConverterFactory(vararg factory: Converter.Factory) {
        converterFactories += factory
    }

    @JvmStatic
    fun removeConverterFactory(vararg factory: Converter.Factory) {
        converterFactories -= factory
    }

    @JvmStatic
    fun removeAllConverterFactories() {
        converterFactories.clear()
    }

    @JvmStatic
    fun addCallAdapterFactory(vararg factory: CallAdapter.Factory) {
        callAdapterFactories += factory
    }

    @JvmStatic
    fun removeCallAdapterFactory(vararg factory: CallAdapter.Factory) {
        callAdapterFactories -= factory
    }

    @JvmStatic
    fun removeAllCallAdapterFactories() {
        callAdapterFactories.clear()
    }

    @JvmStatic
    fun addInterceptor(vararg interceptor: Interceptor) {
        interceptors += interceptor
    }

    @JvmStatic
    fun removeInterceptor(vararg interceptor: Interceptor) {
        interceptors -= interceptor
    }

    @JvmStatic
    fun removeAllInterceptors() {
        interceptors.clear()
    }

    @JvmStatic
    fun <T> create(serviceClazz: Class<T>): T {
        return retrofit.create(serviceClazz)
    }
}