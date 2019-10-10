package kr.saintdev.dcalarm.modules.km

import android.content.Context
import kr.saintdev.dcalarm.modules.database.DatabaseManager
import kr.saintdev.dcalarm.modules.database.SQLQueries


class KeywordManager {
    companion object {
        private var ins: KeywordManager? = null

        fun getInstance(context: Context): KeywordManager {
            if (ins == null) {
                ins = KeywordManager(context)
            }

            return ins!!
        }
    }

    private val context: Context
    private val databaseManager: DatabaseManager

    private constructor(context: Context) {
        this.context = context
        this.databaseManager = DatabaseManager.getInstance()
    }

    /**
     * @Date 10.10 2019
     * return All Keywords.
     * @return ArrayList<Keyword>       Keyword Array.
     */
    fun readAllKeywords() : ArrayList<Keyword> {
        val cursor = this.databaseManager.executeQuery(SQLQueries.SELECT_KEYWORD_ALL, this.context, null)
        val keywords = ArrayList<Keyword>()

        while(cursor.moveToNext()) {
            val tmp = Keyword(cursor.getInt(0), cursor.getString(1))
            keywords.add(tmp)
        }

        cursor.close()
        return keywords
    }

    fun readKeyword(id: Int) : Keyword? {
        val cursor = this.databaseManager.executeQuery(SQLQueries.SELECT_KEYWORD_WHERE_ID, this.context, arrayOf(id.toString()))

        if(cursor.moveToNext()) {
            val tmp =  Keyword(cursor.getInt(1), cursor.getString(2))
            cursor.close()
            return tmp
        }

        return null
    }

    fun removeKeyword(id: Int) {
        val stmt = this.databaseManager.makeReadQuery(SQLQueries.DELETE_KEYWORD_WHERE_ID, this.context)
        stmt.bindLong(1, id.toLong())
        stmt.execute()
    }

    fun insertKeyword(keyword: Keyword) {
        val stmt = this.databaseManager.makeInsertQuery(SQLQueries.INSERT_KEYWORD, this.context)
        stmt.bindString(1, keyword.keyword)
        stmt.execute()
    }

    fun isExsit(k: Keyword) : Boolean {
        val keywords = readAllKeywords()

        for(kd in keywords) {
            if(kd.keyword == k.keyword) return true
        }

        return false
    }
}