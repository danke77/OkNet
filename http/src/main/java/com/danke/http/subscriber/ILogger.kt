package com.danke.http.subscriber

/**
 * @author danke
 * @date 2018/6/26
 */
interface ILogger {
    fun log(tag: String, msg: String?, t: Throwable?)
}