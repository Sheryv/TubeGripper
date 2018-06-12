package com.sheryv.tubegripper

import com.sheryv.tubegripper.util.Constants
import okhttp3.*
import okio.*
import java.io.IOException
import okio.Okio
import java.io.File

typealias ProgressListener = (bytesRead: Long, contentLength: Long, done: Boolean) -> Unit

class Downloader(val request: Request, val progressListener: ProgressListener? = null) {
    constructor(url: String, progressListener: ProgressListener? = null) :
            this(Request.Builder().url(url)
                    .header("User-Agent", Constants.USER_AGENT)
                    .build(), progressListener)


    public var updatePerPercent = false

    public fun getString(): String {
        start().use { response ->
            if (!response.isSuccessful) throw IOException("Wrong response: $response")
            return response.body()!!.string()
        }
    }

    public fun getFile(filename: String): File {
        start().use { response ->
            if (!response.isSuccessful) throw IOException("Wrong response: $response")

            val downloadedFile = File(filename)
            val sink = Okio.buffer(Okio.sink(downloadedFile))
            sink.writeAll(response.body()!!.source())
            sink.close()
            return downloadedFile
        }
    }


    private fun start(): Response {
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val originalResponse = chain.proceed(chain.request())
                    originalResponse.newBuilder()
                            .body(ProgressResponseBody(originalResponse.body(), progressListener, updatePerPercent))
                            .build()
                }
                .build()
        return client.newCall(request).execute()
    }


    /* val progressListener = object : ProgressListener {
         internal var firstUpdate = true

         override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
             if (done) {
                 println("completed")
             } else {
                 if (firstUpdate) {
                     firstUpdate = false
                     if (contentLength == -1L) {
                         println("content-length: unknown")
                     } else {
                         System.out.format("content-length: %d\n", contentLength)
                     }
                 }

                 println(bytesRead)

                 if (contentLength != -1L) {
                     System.out.format("%d%% done\n", 100 * bytesRead / contentLength)
                 }
             }
         }
     }*/


    private class ProgressResponseBody
    constructor(private val responseBody: ResponseBody?, private val progressListener: ProgressListener?, private val updatePerPercent: Boolean) : ResponseBody() {

        private val bufferedSource: BufferedSource by lazy { Okio.buffer(source(responseBody!!.source())) }


        override fun contentType(): MediaType? {
            return responseBody?.contentType()
        }

        override fun contentLength(): Long {
            return responseBody?.contentLength() ?: 0
        }

        override fun source(): BufferedSource {
            return bufferedSource
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                internal var totalBytesRead = 0L
                internal var nextUpdate = 0L


                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    val len = responseBody?.contentLength() ?: 0
                    if (!updatePerPercent || len <= 0 || totalBytesRead >= nextUpdate) {
                        progressListener?.invoke(totalBytesRead, len, bytesRead == -1L)
                        if (updatePerPercent && len > 0) {
                            nextUpdate = len / 100 + totalBytesRead
                        }
                    }
                    return bytesRead
                }
            }
        }
    }

//    interface ProgressListener {
//        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
//    }


}