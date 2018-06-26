package com.danke.http.subscriber

import android.content.Context
import android.support.annotation.CallSuper
import com.danke.http.R
import com.danke.http.adapter.rxjava2.RetrofitException
import com.danke.http.exception.AppErrorException
import com.danke.http.exception.HttpErrorException
import com.danke.http.exception.ResponseErrorException
import com.danke.http.exception.TokenInvalidException
import io.reactivex.subscribers.DefaultSubscriber
import java.lang.ref.WeakReference

/**
 * @author danke
 * @date 2018/6/26
 */
abstract class BaseSubscriber<T> @JvmOverloads constructor(
        context: Context, private val defaultErrorMessage: String? = null) : DefaultSubscriber<T>(), IErrorHandler {

    companion object {
        const val ERROR_CODE_LOCAL = -1
    }

    private val contextRef: WeakReference<Context>? = WeakReference(context)

    protected val context: Context?
        get() = contextRef?.get()

    fun unsubscribe() {
        cancel()
    }

    override fun onComplete() {
    }

    @CallSuper
    override fun onError(t: Throwable) {
        when (t) {
            is IllegalStateException -> {
                // ignore
            }
            is HttpErrorException -> onHttpError(t)
            is TokenInvalidException -> onTokenInvalid(t)
            is ResponseErrorException -> onResponseError(t)
            is RetrofitException -> onRetrofitError(t)
            else -> onAppError(AppErrorException(ERROR_CODE_LOCAL, t.message
                    ?: defaultErrorMessage
                    ?: context?.getString(R.string.http_request_default_error_message), t))
        }

        // onComplete()
    }
}