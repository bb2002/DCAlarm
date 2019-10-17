package kr.saintdev.dcalarm.modules.parser

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

const val DC_GALL_URL = "https://gall.dcinside.com"

data class PostMeta(
    val uuid: String,
    val url: String,
    val title: String,
    val writer: String,
    val date: Date?,
    val viewCount: Int
)

data class GalleryMeta(
    val galleryName: String,
    val galleryID: String,
    val wdate: String?
)

data class NotificationAlarmMeta(
    val post: PostMeta,
    val ndate: Date
)

//fun Date.str() : String {
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    return sdf.format(this)
//}

//fun PostMeta.print() {
//    Log.e("DCWebParser", "=-= ARTICLE ID : " + this.uuid)
//    Log.e("DCWebParser", "URL : " + this.url)
//    Log.e("DCWebParser", "TITLE : " + this.title)
//    Log.e("DCWebParser", "WRITER : " + this.writer)
//    Log.e("DCWebParser", "DATE : " + this.date.str())
//    Log.e("DCWebParser", "VIEWCOUNT : " + this.viewCount)
//    Log.e("DCWebParser", "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=")
//}