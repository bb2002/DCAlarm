package kr.saintdev.dcalarm.modules

import java.text.SimpleDateFormat
import java.util.*

object DateUtilFunctions {
    /**
     * @Date 09.26 2019
     * Format 0000-00-00 00:00:00
     * if null, return now
     */
    fun stringToDate(str: String) : Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.parse(str) ?: Date()
    }

    fun getNowToString() : String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(Date())
    }

}