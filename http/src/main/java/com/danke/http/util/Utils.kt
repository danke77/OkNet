package com.danke.http.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.webkit.WebSettings


/**
 * @author danke
 * @date 2018/6/23
 */

fun isNetworkAvailable(context: Context?): Boolean {
    context?.let {
        val connectivityManager = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable
    }

    return false
}

fun getDefaultUserAgent(context: Context): String {

    val userAgent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        WebSettings.getDefaultUserAgent(context)
    } else {
        System.getProperty("http.agent")
    }

    val length = userAgent.length
    val sb = StringBuilder()
    for (i in 0 until length) {
        val c = userAgent[i]
        if (c <= '\u001f' || c >= '\u007f') {
            sb.append(Uri.encode(c.toString()))
        } else {
            sb.append(c)
        }
    }

    return sb.toString()
}