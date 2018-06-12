package com.sheryv.tubegripper.model

import com.sheryv.tubegripper.util.Util

class Item(val uuid: String, val title: String, val videos: List<Video>,
           val expire: Long = 0, val thumbnailUrl: String? = null, val author: String? = null) {

    var playlist: String? = null
    var savePath: String? = null
    var other: String? = null

    fun link() = "https://www.youtube.com/watch?v=$uuid"
//    val number: Int = 0
//    val provider: Int = 0

//    val secondTrack: Long = 0
    //    val id: Long = 0

//    @Transient
//    private val error: String? = null
//    @Transient
//    private val expireDateString: String? = null
//
//    @Transient
//    private val callback: Future<File>? = null
//    @Transient
//    private val progressData: ProgressData? = null
//    @Transient
//    private val audio: VideoItem? = null
//    @Transient
//    private val isDownloading: Boolean = false

    fun isExpired(): Boolean {
        return expire * 1000 < System.currentTimeMillis()
    }


    // YT thumbs
    // maxresdefault.jpg
    // hqdefault.jpg
    // mqdefault.jpg



    class FormatData

    /**
     * @param ext             extension
     * @param height          video height
     * @param vCodec          video codec
     * @param fps             fps
     * @param aCodec          audio codec
     * @param audioBitrate    bitrate
     * @param isDashContainer is video composed with 2 tracks
     */
    constructor(
            val name: String,
            val ext: String,
            val height: Int,
            val vCodec: VCodec,
            val aCodec: ACodec,
            val isDashContainer: Boolean,
            val isPrefered: Boolean,
            val audioBitrate: Int = -1,
            val fps: Int = 30
    ) {

        //todo do przetlumaczenia

        val fullName: String
            get() {
                var nn = name
                if (isDashContainer) {
                    nn += if (vCodec === VCodec.NONE)
                        " (Only audio)"
                    else
                        " (Only video)"
                }
                return nn
            }

        val isVideoWithoutAudio: Boolean
            get() = isDashContainer && vCodec !== VCodec.NONE && aCodec === ACodec.NONE
        val isOnlyAudio: Boolean
            get() = isDashContainer && aCodec !== ACodec.NONE


        override fun toString(): String {
            return String.format("%s | %d@%s | video > %s@%d | audio > %s@%d", name, height, ext, vCodec, fps, aCodec, audioBitrate)
        }

    }

    enum class VCodec {
        H263, H264, MPEG4, VP8, VP9, NONE
    }

    enum class ACodec {
        MP3, AAC, VORBIS, OPUS, NONE
    }

    enum class Status(id: Int) {
        GET_INFO(1),
        QUEUE(2),
        WAITING(4),
        DOWNLOADING(8),
        FINISHED(1024),
        CANCELED(2048),
        ERROR(4096);
    }

    override fun toString(): String {
        val s = StringBuilder()
                .append("Video item      = ").append(uuid)
                .append("\n  > Link      = ").append(link())
                .append("\n  > Title     = ").append(title)
                .append("\n  > SavePath  = ").append(savePath)
                .append("\n  > Thumbnail = ").append(thumbnailUrl)
                .append("\n  > Expire    = ").append(expire).append(" | ").append(Util.getDateString(expire*1000, false, ""))
                .append("\n  > Author    = ").append(author)
                .append("\n  -----------")
        for (it in videos) {
            s.append("\n -> Itag      = ").append(it.itag)
                    .append("\n  > Status    = ").append(it.status)
                    .append("\n  > Duration  = ").append(it.duration).append(" | ").append(it.durationString())
                    .append("\n  > Size      = ").append(it.size)
                    .append("\n  > Format    = ").append(it.formatData)
                    .append("\n  -----------")
        }
//                .append("Video item      = ").append(getId())
//                .append("\n  > Uuid      = ").append(getUuid())
//                .append("\n  > Link      = ").append(getVideoLink())
//                .append("\n  > Error     = ").append(getErrorString())
//                .append("\n  > Url       = ").append(getUrl().substring(0, 45)).append("...")
//                .append("\n  > Title     = ").append(getTitle())
//                .append("\n  > Playlist  = ").append(getPlaylist())
//                .append("\n  > SavePath  = ").append(getSavePath())
//                .append("\n  > Thumbnail = ").append(getThumbnailUrl())
//                .append("\n  > Status    = ").append(getStatusName())
//                .append("\n  > Provider  = ").append(getProvider())
//                .append("\n  > Number    = ").append(getNumber())
//                .append("\n  > Duration  = ").append(getDuration())
//                .append("\n  > Size      = ").append(getSize())
//                .append("\n  > Expire    = ").append(getExpire())
//                .append("\n  > ExpireStr = ").append(getExpireDateString())
//                .append("\n  > Author    = ").append(getAuthor())
//                .append("\n  > ITag      = ").append(getItag())
//                .append("\n  > Format    = ").append(getFormat())
//                .append("\n  > isDown    = ").append(isDownloading())
//                .append("\n  > Track     = ").append(getSecondTrack())
//                .append("\n  > Audio     = ").append(if (getAudio() != null) "has" else "null")
//                .append("\n  > Other     = ").append(getOther()).toString()

        return s.toString()
    }

    companion object {
        const val TRACK_NO_AUDIO_TRACK_NEEDED = 0

        @JvmStatic
        val FORMAT_MAP = HashMap<Int, FormatData>()

        init {
            FORMAT_MAP.put(0, FormatData("none", "mp4", 240, VCodec.H264, ACodec.AAC, false, false, 64));
            // Video and Audio
            FORMAT_MAP.put(17, FormatData("144p MPEG-4 24k AAC", "3gp", 144, VCodec.MPEG4, ACodec.AAC, false, false, 24));
            FORMAT_MAP.put(36, FormatData("240p MPEG-4 36k AAC", "3gp", 240, VCodec.MPEG4, ACodec.AAC, false, false, 32));
            FORMAT_MAP.put(5, FormatData("240p H.263 64k MP3", "flv", 240, VCodec.H263, ACodec.MP3, false, false, 64));
            FORMAT_MAP.put(43, FormatData("360p VP8 128k Vorbis", "webm", 360, VCodec.VP8, ACodec.VORBIS, false, false, 128));
            FORMAT_MAP.put(18, FormatData("360p H.264 96k AAC", "mp4", 360, VCodec.H264, ACodec.AAC, false, true, 96));
            FORMAT_MAP.put(22, FormatData("720p H.264 192k AAC", "mp4", 720, VCodec.H264, ACodec.AAC, false, true, 192));

            // Dash Video
            FORMAT_MAP.put(160, FormatData("144p H.264", "mp4", 144, VCodec.H264, ACodec.NONE, true, false));
            FORMAT_MAP.put(133, FormatData("240p H.264", "mp4", 240, VCodec.H264, ACodec.NONE, true, false));
            FORMAT_MAP.put(134, FormatData("360p H.264", "mp4", 360, VCodec.H264, ACodec.NONE, true, false));
            FORMAT_MAP.put(135, FormatData("480p H.264", "mp4", 480, VCodec.H264, ACodec.NONE, true, false));
            FORMAT_MAP.put(136, FormatData("720p H.264", "mp4", 720, VCodec.H264, ACodec.NONE, true, false));
            FORMAT_MAP.put(137, FormatData("1080p H.264", "mp4", 1080, VCodec.H264, ACodec.NONE, true, true));
            FORMAT_MAP.put(264, FormatData("1440p H.264", "mp4", 1440, VCodec.H264, ACodec.NONE, true, true));
            FORMAT_MAP.put(266, FormatData("2160p H.264", "mp4", 2160, VCodec.H264, ACodec.NONE, true, true));
            FORMAT_MAP.put(298, FormatData("720p 60fps H.264", "mp4", 720, VCodec.H264, ACodec.NONE, true, true, fps = 60));
            FORMAT_MAP.put(299, FormatData("1080p 60fps H.264", "mp4", 1080, VCodec.H264, ACodec.NONE, true, true, fps = 60));

            // Dash Audio
            FORMAT_MAP.put(140, FormatData("128k AAC", "m4a", -1, VCodec.NONE, ACodec.AAC, true, true, 128));
            FORMAT_MAP.put(141, FormatData("256k AAC", "m4a", -1, VCodec.NONE, ACodec.AAC, true, true, 256));

            // WEBM Dash Video
            FORMAT_MAP.put(278, FormatData("144p VP9", "webm", 144, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(242, FormatData("240p VP9", "webm", 240, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(243, FormatData("360p VP9", "webm", 360, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(244, FormatData("480p VP9", "webm", 480, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(247, FormatData("720p VP9", "webm", 720, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(248, FormatData("1080p VP9", "webm", 1080, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(271, FormatData("1440p VP9", "webm", 1440, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(313, FormatData("2160p VP9", "webm", 2160, VCodec.VP9, ACodec.NONE, true, false));
            FORMAT_MAP.put(302, FormatData("720p 60fps VP9", "webm", 720, VCodec.VP9, ACodec.NONE, true, false, fps = 60));
            FORMAT_MAP.put(308, FormatData("1440p 60fps VP9", "webm", 1440, VCodec.VP9, ACodec.NONE, true, false, fps = 60));
            FORMAT_MAP.put(303, FormatData("1080p 60fps VP9", "webm", 1080, VCodec.VP9, ACodec.NONE, true, false, fps = 60));
            FORMAT_MAP.put(315, FormatData("2160p 60fps VP9", "webm", 2160, VCodec.VP9, ACodec.NONE, true, true, fps = 60));

            // WEBM Dash Audio
            FORMAT_MAP.put(171, FormatData("128k Vorbis", "webm", -1, VCodec.NONE, ACodec.VORBIS, true, false, 128));
            FORMAT_MAP.put(249, FormatData("50k Opus", "webm", -1, VCodec.NONE, ACodec.OPUS, true, false, 48));
            FORMAT_MAP.put(250, FormatData("70k Opus", "webm", -1, VCodec.NONE, ACodec.OPUS, true, false, 64));
            FORMAT_MAP.put(251, FormatData("160k Opus", "webm", -1, VCodec.NONE, ACodec.OPUS, true, false, 160));

            // 3D
            FORMAT_MAP.put(84, FormatData("720p 3D 192k AAC", "mp4", 720, VCodec.H264, ACodec.AAC, false, true, 192));
            FORMAT_MAP.put(82, FormatData("360p 3D 96k AAC", "mp4", 360, VCodec.H264, ACodec.AAC, false, true, 96));
            FORMAT_MAP.put(100, FormatData("360p 3D 128k Vorbis", "mp4", 360, VCodec.H264, ACodec.VORBIS, false, true, 192));
        }
    }

}
