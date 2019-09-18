package kr.saintdev.dcalarm.modules.parser

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class PostMeta(
    val uuid: String,
    val url: String,
    val title: String,
    val writer: String,
    val date: Date,
    val viewCount: Int
)