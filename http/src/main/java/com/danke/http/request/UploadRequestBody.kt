package com.danke.http.request

import android.os.Handler
import android.os.Looper

import java.io.File
import java.io.FileInputStream
import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

/**
 * @author danke
 * @date 2018/7/4
 */
class UploadRequestBody @JvmOverloads constructor(
        private val file: File,
        private val type: String = "image/*",
        private val callbacks: UploadCallbacks? = null) : RequestBody() {

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)

        fun onError()

        fun onFinish()
    }

    override fun contentType(): MediaType? = MediaType.parse(type)

    @Throws(IOException::class)
    override fun contentLength(): Long = file.length()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded: Long = 0

        var read: Int = inputStream.read(buffer)
        val handler = Handler(Looper.getMainLooper())
        while (read != -1) {

            // update progress on UI thread
            handler.post(ProgressUpdater(uploaded, fileLength))

            uploaded += read.toLong()
            sink.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
        inputStream.close()
    }

    private inner class ProgressUpdater(private val uploaded: Long, private val total: Long) : Runnable {

        override fun run() {
            callbacks?.onProgressUpdate((100 * uploaded / total).toInt())
        }
    }
}
