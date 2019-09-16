package kr.saintdev.dcalarm.modules.parser

import java.time.LocalDate
import java.time.LocalDateTime

data class PostMeta(
    val uuid: String,
    val url: String,
    val title: String,
    val writer: String,
    val date: LocalDateTime,
    val viewCount: Int
)