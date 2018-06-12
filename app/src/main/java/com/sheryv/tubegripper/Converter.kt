package com.sheryv.tubegripper

import com.google.gson.Gson
import com.sheryv.tubegripper.model.Item
import com.sheryv.tubegripper.model.Path
import com.sheryv.tubegripper.model.Video
import com.sheryv.tubegripper.util.Constants
import com.sheryv.tubegripper.util.Util
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.file.Files.size
import java.util.regex.Pattern


class Converter {

    @Throws
    fun convert(videoLink: String): Item {
        val id = Util.parseLink(videoLink)
                ?: throw IllegalArgumentException("Video link has incorrect format: $videoLink")
        val data = fetchData(videoLink)
        return parseItem(data, id)
    }

    fun parseItem(data: String, id: String): Item {
        val parts = data.split('[', ']')
        val inside = parts.first { it.startsWith('{') && it.contains("googlevideo") }.replace("\\", "")
        val description = "{" + parts.last { it.contains("\"thumb\"") }.substring(2)


        val p = Pattern.compile("\"url\":\"([_\\\\?=:&#,.A-Za-z0-9/-]+)\"")
        val matcher = p.matcher(inside)

        val videos = ArrayList<Video>()
        var expire: Long = 0
        while (matcher.find()) {
            println("\nStart index: " + matcher.start())
            println(" End index: " + matcher.end() + " ")


            var url: String? = null
            var audio: String? = null
            var size: Long = 0L
            var audiosize: Long = 0L
            for (it in matcher.group().split("#")) {
                if (it.startsWith("\"url") || it.startsWith("url")) {
                    url = it.substring(7)
                    if (url.endsWith("&")) {
                        url = url.substring(0, url.length - 1)
                    }
                    println(url)


                } else if (it.startsWith("audio=") || it.startsWith("\"audio=")) {
                    audio = it.substring(7)
                } else if (it.startsWith("size=") || it.startsWith("\"size=")) {
                    size = it.split("=")[1].trimEnd('"').toLong()
                } else if (it.startsWith("audiosize=") || it.startsWith("\"audiosize=")) {
                    audiosize = it.split("=")[1].trimEnd('"').toLong()
                }
            }
            if (url != null) {
                val a = audio?.let { Path(it, 0f, 0, audiosize) }
                val path = parse(url)
                if (path.size> 0)
                    size = path.size
                val video = Video(path.url, path.dur, path.itag, size, a)
                expire = path.expire
                videos.add(video)
            }

        }

        val g = Gson()
        val desc = g.fromJson(description, Description::class.java)
        val item = Item(id, desc.filename, videos, expire = expire, author = desc.channel, thumbnailUrl = desc.thumb)
        item.other = desc.videoid
        return item
    }


    fun fetchData(videoLink: String): String {
        val url = "https://www.clipconverter.cc/check.php"
        val body = FormBody.Builder().add("mediaurl", videoLink).build()
        val req = Request.Builder().url(url)
                .header("User-Agent", Constants.USER_AGENT).post(body).build()
        val d = Downloader(req)
        return d.getString()
    }


    private fun parse(line: String): Parsed {
        var dur = 0f
        var itag = 0
        var size: Long = 0
        var expire: Long = 0
        line.split("?")[1].split("&").forEach {
            val parts = it.split("=")
            if (parts[0] == "dur")
                dur = parts[1].toFloat()
            else if (parts[0] == "clen")
                size = parts[1].toLong()
            else if (parts[0] == "itag")
                itag = parts[1].toInt()
            else if (parts[0] == "expire")
                expire = parts[1].toLong()
        }

        return Parsed(line, dur, itag, size, expire)
    }

    class Parsed(val url: String, val dur: Float = 0f,
                 val itag: Int = 0,
                 val size: Long = 0, val expire: Long
    )

    class Description(val filename: String,
                      val thumb: String,
                      val channel: String,
                      val videoid: String
    )
}