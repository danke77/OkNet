package com.danke.http.exception

/**
 * @author danke
 * @date 2018/6/26
 */
open class BaseException @JvmOverloads constructor(
        val code: Int, val msg: String?, val throwable: Throwable? = null) : RuntimeException(msg, throwable)