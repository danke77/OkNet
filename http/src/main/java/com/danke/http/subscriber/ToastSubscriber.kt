package com.danke.http.subscriber

import android.content.Context
import android.widget.Toast
import com.danke.http.adapter.rxjava2.RetrofitException
import com.danke.http.exception.AppErrorException
import com.danke.http.exception.HttpErrorException
import com.danke.http.exception.ResponseErrorException
import me.drakeet.support.toast.ToastCompat

/**
 * @author danke
 * @date 2018/6/26
 */
abstract class ToastSubscriber<T> @JvmOverloads constructor(
        context: Context, private val toast: IToast? = null) : BaseSubscriber<T>(context) {

    override fun onHttpError(e: HttpErrorException) {
        toast(e.message)
    }

    override fun onResponseError(e: ResponseErrorException) {
        toast(e.message)
    }

    override fun onRetrofitError(e: RetrofitException) {
        toast(e.message)
    }

    override fun onAppError(e: AppErrorException) {
        toast(e.message)
    }

    private fun toast(message: String?) {
        context?.let {
            if (toast != null) {
                toast.show(it, message ?: "")
            } else {
                ToastCompat.makeText(it, message ?: "", Toast.LENGTH_SHORT).show()
            }
        }
    }
}