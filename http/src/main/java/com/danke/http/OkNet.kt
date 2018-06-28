package com.danke.http

import retrofit2.Retrofit

/**
 * @author danke
 * @date 2018/6/19
 */
object OkNet {

    private const val DEFAULT_RETROFIT_KEY = "DEFAULT_RETROFIT"

    private val retrofitMap = mutableMapOf<String, Retrofit>()

    @JvmStatic
    var baseUrl: String = ""

    @JvmStatic
    var retrofitBuilder: RetrofitBuilder? = null

    @JvmStatic
    @JvmOverloads
    fun createRetrofit(key: String? = null, builder: RetrofitBuilder): Retrofit {
        val retrofit = builder.build()
        retrofitMap[key ?: builder.baseUrl] = retrofit

        return retrofit
    }

    @JvmStatic
    @JvmOverloads
    fun get(key: String = DEFAULT_RETROFIT_KEY): Retrofit {
        if (key == DEFAULT_RETROFIT_KEY) {
            var retrofit = retrofitMap[key]
            if (retrofit == null) {
                retrofit = createRetrofit(DEFAULT_RETROFIT_KEY,
                        retrofitBuilder ?: RetrofitBuilder(baseUrl))
                retrofitMap[key] = retrofit
            }
            return retrofit
        }

        return retrofitMap[key]
                ?: throw RuntimeException("There's no Retrofit for the giving key, please create it first!")
    }

    @JvmStatic
    fun <T> create(serviceClazz: Class<T>): T {
        return get().create(serviceClazz)
    }
}