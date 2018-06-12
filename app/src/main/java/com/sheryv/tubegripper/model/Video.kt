package com.sheryv.tubegripper.model

class Video(url: String, duration: Float, itag: Int, size: Long, var audio: Path? = null) : Path(url, duration, itag, size) {

}