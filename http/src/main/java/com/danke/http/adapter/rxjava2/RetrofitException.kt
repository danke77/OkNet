package com.danke.http.adapter.rxjava2

import com.danke.http.converter.gson.JsonParseException
import okhttp3.Request
import retrofit2.Response
import java.io.IOException
import retrofit2.Retrofit


/**
 * @author danke
 * @date 2018/6/21
 */
class RetrofitException private constructor(
        /** The event kind which triggered this error. */
        val kind: Kind,
        /** The Retrofit this request was executed on */
        val retrofit: Retrofit,
        /** The request which produced the error. */
        val request: Request,
        /** Response object containing status code, headers, body, etc. */
        val response: Response<*>?,
        val msg: String?,
        val throwable: Throwable?) : RuntimeException(msg, throwable) {

    companion object {

        @JvmStatic
        fun networkError(retrofit: Retrofit, request: Request, exception: IOException): RetrofitException {
            return RetrofitException(Kind.NETWORK, retrofit, request, null, exception.message, exception)
        }

        @JvmStatic
        fun httpError(retrofit: Retrofit, request: Request, response: Response<*>): RetrofitException {
            return RetrofitException(Kind.HTTP, retrofit, request, response,
                    String.format("%s(%d)", response.message(), response.code()), null)
        }

        @JvmStatic
        fun timeOutError(retrofit: Retrofit, request: Request, exception: Throwable): RetrofitException {
            return RetrofitException(Kind.TIME_OUT, retrofit, request, null, exception.message, exception)
        }

        @JvmStatic
        fun jsonParseError(retrofit: Retrofit, request: Request, exception: JsonParseException): RetrofitException {
            return RetrofitException(Kind.PARSE_ERROR, retrofit, request, null, exception.message, exception)
        }

        @JvmStatic
        fun unexpectedError(retrofit: Retrofit, request: Request, exception: Throwable): RetrofitException {
            return RetrofitException(Kind.UNEXPECTED, retrofit, request, null, exception.message, exception)
        }
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        response?.errorBody()?.let {
            val converter = retrofit.responseBodyConverter<T>(type, arrayOfNulls<Annotation>(0))
            return converter.convert(it)
        }

        return null
    }

    /** Identifies the event kind which triggered a [RetrofitException].  */
    enum class Kind {
        /** An [IOException] occurred while communicating to the server. */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        HTTP,
        TIME_OUT,
        PARSE_ERROR,
        /**
         * An internal error occurred while attempting to execute a request.
         * It is best practice to re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }
}