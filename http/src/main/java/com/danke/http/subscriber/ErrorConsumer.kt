package com.danke.http.subscriber

import android.support.annotation.IntDef
import io.reactivex.functions.Consumer


/**
 * @author danke
 * @date 2018/6/26
 */
class ErrorConsumer private constructor(
        @Type private val type: Long = NONE, private val logger: ILogger? = null) : Consumer<Throwable> {

    companion object {
        const val NONE = 0x0L
        const val LOG = 0x1L
        const val PRINT = 0x2L

        private const val TAG = "ErrorConsumer"

        @JvmStatic
        @JvmOverloads
        fun obtain(@Type type: Long = NONE): ErrorConsumer = ErrorConsumer(type)

        @JvmStatic
        fun obtain(logger: ILogger?): ErrorConsumer = ErrorConsumer(LOG, logger)

        @JvmStatic
        fun obtain(@Type type: Long, logger: ILogger? = null): ErrorConsumer = ErrorConsumer(type, logger)
    }

    @IntDef(NONE, LOG, PRINT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Type

    override fun accept(t: Throwable?) {
        when (type) {
            NONE -> {
            }
            LOG -> logger?.log(TAG, t?.message, t)
            PRINT -> t?.printStackTrace()
        }
    }
}