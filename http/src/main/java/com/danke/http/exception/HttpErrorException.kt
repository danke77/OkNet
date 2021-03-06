package com.danke.http.exception

/**
 * @author danke
 * @date 2018/6/26
 */
class HttpErrorException @JvmOverloads constructor(
        code: Int, msg: String?, throwable: Throwable? = null) : BaseException(code, msg, throwable)