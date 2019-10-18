package kr.saintdev.dcalarm.modules.sharedprep

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SettingsFunction {
    const val SHARED_PREP_NAME = "dcalarm.settings"

    const val PARSEING_SERVICE_DELAY_NAME = "dcalarm.settings.pdelay"
    const val WORK_ON_DATA_MODE_NAME = "dcalarm.settings.usedata"


    fun getParseingServiceDelay(context: Context) : Int {
        val sharedPrep = context.getSharedPreferences(SHARED_PREP_NAME, MODE_PRIVATE)
        return sharedPrep.getInt(PARSEING_SERVICE_DELAY_NAME, 10)
    }

    fun setParseingServiceDelay(context: Context, int: Int) {
        val sharedPrep = context.getSharedPreferences(SHARED_PREP_NAME, MODE_PRIVATE).edit()
        sharedPrep.putInt(PARSEING_SERVICE_DELAY_NAME, int)
        sharedPrep.apply()
    }

    fun isWorkOnDataMode(context: Context) : Boolean {
        val sharedPrep = context.getSharedPreferences(SHARED_PREP_NAME, MODE_PRIVATE)
        return sharedPrep.getBoolean(WORK_ON_DATA_MODE_NAME, false)
    }

    fun setWorkOnDataMode(context: Context, bool: Boolean) {
        val sharedPrep = context.getSharedPreferences(SHARED_PREP_NAME, MODE_PRIVATE).edit()
        sharedPrep.putBoolean(WORK_ON_DATA_MODE_NAME, bool)
        sharedPrep.apply()
    }
}