package com.danke.http.subscriber

import android.content.Context

/**
 * @author danke
 * @date 2018/6/26
 */
interface IToast {
    fun show(context: Context, message: String)
}