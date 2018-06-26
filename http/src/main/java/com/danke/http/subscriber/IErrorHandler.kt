package com.danke.http.subscriber

import com.danke.http.adapter.rxjava2.RetrofitException
import com.danke.http.exception.AppErrorException
import com.danke.http.exception.HttpErrorException
import com.danke.http.exception.ResponseErrorException
import com.danke.http.exception.TokenInvalidException

/**
 * @author danke
 * @date 2018/6/26
 */
interface IErrorHandler {
    fun onHttpError(e: HttpErrorException)

    fun onTokenInvalid(e: TokenInvalidException)

    fun onResponseError(e: ResponseErrorException)

    fun onRetrofitError(e: RetrofitException)

    fun onAppError(e: AppErrorException)
}