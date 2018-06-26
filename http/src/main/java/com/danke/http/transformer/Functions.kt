package com.danke.http.transformer

import com.danke.http.exception.HttpErrorException
import com.danke.http.exception.ResponseErrorException
import com.danke.http.exception.TokenInvalidException
import com.danke.http.response.BaseResponse
import io.reactivex.functions.Function
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/26
 */
internal fun <T : Response<R>, R : BaseResponse<*>> errorChecker(
        errorChecker: IErrorChecker<R>): Function<T, R> =
        Function { t ->
            val body = t.body()
            val code = t.code()
            val msg = t.message()

            // http success
            if (t.isSuccessful && body != null) {
                // token invalid
                if (errorChecker.isTokenInvalid(body)) {
                    throw TokenInvalidException(code, msg)
                }
                // business fail
                else if (errorChecker.isResponseError(body)) {
                    throw ResponseErrorException(code, msg)
                }
                // business success
            }
            // http fail
            else {
                throw HttpErrorException(code, msg)
            }

            body
        }