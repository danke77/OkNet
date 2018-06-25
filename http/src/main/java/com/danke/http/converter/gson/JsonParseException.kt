package com.danke.http.converter.gson

import java.io.IOException
import java.io.PrintStream
import java.io.PrintWriter

/**
 * @author danke
 * @date 2018/6/25
 */
class JsonParseException constructor(val json: String, val throwable: Throwable?) : IOException() {

    override val message: String?
        get() = throwable?.message ?: super.message

    override fun getStackTrace(): Array<StackTraceElement> =
            throwable?.stackTrace ?: super.getStackTrace()

    override fun setStackTrace(stackTrace: Array<StackTraceElement>) {
        if (throwable != null) {
            throwable.stackTrace = stackTrace
        } else {
            super.setStackTrace(stackTrace)
        }
    }

    override fun printStackTrace() {
        throwable?.printStackTrace()
    }

    override fun printStackTrace(s: PrintStream) {
        throwable?.printStackTrace(s)
    }

    override fun printStackTrace(s: PrintWriter) {
        throwable?.printStackTrace(s)
    }
}