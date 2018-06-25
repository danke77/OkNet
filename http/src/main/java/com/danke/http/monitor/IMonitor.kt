package com.danke.http.monitor

import okhttp3.Request

/**
 * @author danke
 * @date 2018/6/25
 */
interface IMonitor {
    fun monitor(request: Request, tookMs: Long, contentLength: Long)
}