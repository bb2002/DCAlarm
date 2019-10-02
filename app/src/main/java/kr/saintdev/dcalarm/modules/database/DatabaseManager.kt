package kr.saintdev.dcalarm.modules.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.provider.ContactsContract

class DatabaseManager {
    private var helper: DatabaseHelper? = null

    companion object {
        private var ins: DatabaseManager? = null

        fun getInstance() : DatabaseManager {
            if(ins == null) ins = DatabaseManager()
            return ins!!
        }
    }

    private fun initHelper(context: Context) {
        if(this.helper == null) {
            this.helper = DatabaseHelper(context, "dcalarm.db", null, 1)
        }
    }

    fun makeInsertQuery(query: String, context: Context) : SQLiteStatement {
        initHelper(context)
        val writable = this.helper?.writable()!!
        return writable.compileStatement(query)
    }

    fun makeReadQuery(query: String, context: Context) : SQLiteStatement {
        initHelper(context)
        val readable = this.helper?.readable()!!
        return readable.compileStatement(query)
    }

    fun executeQuery(sql: String, args: Array<String>?) : Cursor {
        val readable = this.helper?.readable()!!
        return readable.rawQuery(sql, args)
    }


    private inner class DatabaseHelper(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int) : SQLiteOpenHelper(context, name, factory, version) {


        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(SQLQueries.CREATE_TABLE_METASET)
            p0?.execSQL(SQLQueries.CREATE_TABLE_TARGETING_GALL)
        }

        fun readable() : SQLiteDatabase = this.readableDatabase
        fun writable() : SQLiteDatabase = this.writableDatabase

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        }
    }
}
