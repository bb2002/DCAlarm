package kr.saintdev.dcalarm.modules.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager private constructor() {

    companion object {
        private var ins: DatabaseManager? = null

        fun getInstance() : DatabaseManager {
            if(ins == null) ins = DatabaseManager()
            return ins!!
        }
    }


    private inner class DatabaseHelper(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int) : SQLiteOpenHelper(context, name, factory, version) {


        override fun onCreate(p0: SQLiteDatabase?) {

        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        }
    }
}
