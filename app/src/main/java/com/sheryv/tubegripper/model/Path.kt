package com.sheryv.tubegripper.model

import com.sheryv.tubegripper.util.Util


open class Path(val url: String,
                val duration: Float, val itag: Int, val size: Long) {

    val formatData: Item.FormatData?
        get() = Item.FORMAT_MAP[itag]

    val status: Item.Status = Item.Status.GET_INFO

    fun durationString(): String = Util.durationString(duration)

    fun sizeMB(): Float = Util.sizeMB(size)

}