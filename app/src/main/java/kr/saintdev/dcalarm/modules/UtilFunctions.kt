package kr.saintdev.dcalarm.modules

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*

val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
val DEFAULT_DATE_STRING = "1970-01-01 00:00:00"

object DateUtilFunctions {
    /**
     * @Date 09.26 2019
     * Format 0000-00-00 00:00:00
     * if null, return now
     */
    fun stringToDate(str: String) = str.toDate()

    fun getNowToString(): String = formatter.format(Date())
}

object Networks {
    fun isLTEMode(context: Context) : Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connManager.activeNetworkInfo
        return netInfo?.type == ConnectivityManager.TYPE_MOBILE
    }
}

fun String.toDate() = formatter.parse(this) ?: Date()

fun Date.toFormatString() = formatter.format(this)