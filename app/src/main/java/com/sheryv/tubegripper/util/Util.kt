@file:JvmName("Util")

package com.sheryv.tubegripper.util

import java.io.BufferedReader
import java.io.File
import java.text.DateFormat
import java.util.*


object Util {
    @JvmStatic
    public fun readFile(file: File): String {
        val bufferedReader: BufferedReader = file.bufferedReader()
        return bufferedReader.use { it.readText() }
    }

    @JvmStatic
    fun getDateString(millis: Long, timeLeft: Boolean, expiredTransString:String): String {
        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        val sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        sdf.timeZone = tz
        var s = millis - System.currentTimeMillis()
        s = s / 1000 / 60
        cal.timeInMillis = millis
        var forma = sdf.format(cal.time)
        if (timeLeft) {
            if (s >= 0)
                forma += " | $s min"
            else
                forma += " | $expiredTransString"
        }
        return forma
    }

    @JvmStatic
    fun durationString(duration:Float): String {
        var sec = "0"
        var min = "0"
        if (duration.toInt() % 60 >= 10)
            sec = (duration.toInt() % 60).toString()
        else
            sec += (duration.toInt() % 60).toString()
        if (duration.toInt() / 60 >= 10)
            min = (duration.toInt() / 60).toString()
        else
            min += (duration.toInt() / 60).toString()
        return String.format("%s:%s", min, sec)
    }

    @JvmStatic
    fun sizeMB(size:Long): Float {
        var t = size / 1024 //2510 k
        var s = t % 1024    //510 k
        s /= 102 //5
        t /= 1024   //2
        return t + s.toFloat() / 10
    }

    @JvmStatic
    fun parseLink(videoLink: String): String? {
        if (videoLink.contains("://youtu.be/")) {
            return videoLink.split("be/")[1]
        } else if (videoLink.contains("youtube.com/watch?v=")) {
            return videoLink.split("?v=")[1]
        }
        return null
    }
}

