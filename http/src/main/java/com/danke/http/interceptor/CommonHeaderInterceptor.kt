package com.danke.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author danke
 * @date 2018/6/26
 */
class CommonHeaderInterceptor constructor(val map: MutableMap<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequestBuilder = chain.request().newBuilder()

        map.forEach { key, value -> newRequestBuilder.header(key, value) }

        return chain.proceed(newRequestBuilder.build())
    }

    class Builder {

        private val map = mutableMapOf<String, String>()

        fun header(key: String, value: String): Builder {
            map[key] = value
            return this
        }

        fun header(key: String, value: Int): Builder {
            map[key] = value.toString()
            return this
        }

        fun header(key: String, value: Long): Builder {
            map[key] = value.toString()
            return this
        }

        fun header(key: String, value: Float): Builder {
            map[key] = value.toString()
            return this
        }

        fun header(key: String, value: Double): Builder {
            map[key] = value.toString()
            return this
        }

        fun build(): CommonHeaderInterceptor = CommonHeaderInterceptor(map)

    }
}