package kr.saintdev.dcalarm.modules.database

import android.content.Context
import kr.saintdev.dcalarm.modules.DateUtilFunctions.getNowToString
import kr.saintdev.dcalarm.modules.database.SQLQueries
import kr.saintdev.dcalarm.modules.database.SQLQueries.SELECT_DC_TARGETING_GALLERY_ALL
import kr.saintdev.dcalarm.modules.database.SQLQueries.SELECT_DC_TARGETING_GALLERY_WHERE_ID
import kr.saintdev.dcalarm.modules.parser.GalleryMeta

/**
 * @Date 10.02 2019
 * 새 관리할 갤러리를 DB 에 삽입한다.
 */
fun GalleryMeta.Insert(dbm: DatabaseManager, context: Context) {
    val pst = dbm.makeInsertQuery(SQLQueries.INSERT_DC_TARGETING_GALLERY, context)
    pst.bindString(1, this.galleryName)
    pst.bindString(2, this.galleryID)
    pst.bindString(3, getNowToString())
    pst.executeInsert()
}

fun GalleryMeta.removeFromDB(dbm: DatabaseManager, context: Context) {
    val pst = dbm.makeInsertQuery(SQLQueries.DELETE_DC_TARGETING_GALLERY, context)
    pst.bindString(1, this.galleryID)
    pst.execute()
}

object GalleryMetaDatabaseFunc {
    /**
     * @Date 10.02 2019
     * 관리중인 갤러리의 데이터를 불러 온다.
     */
    fun readAll(context: Context) : ArrayList<GalleryMeta> {
        val dbm = DatabaseManager.getInstance()
        val cursor = dbm.executeQuery(SELECT_DC_TARGETING_GALLERY_ALL, context,null)
        val datas = ArrayList<GalleryMeta>()

        while(cursor.moveToNext()) {
            datas.add(
                GalleryMeta(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
        }

        return datas
    }

    /**
     * @Date 10.07 2019
     * 특정 갤러리의 데이터를 불러 온다.
     */
    fun read(context: Context, gallery: GalleryMeta) : ArrayList<GalleryMeta> {
        val dbm = DatabaseManager.getInstance()
        val cursor = dbm.executeQuery(SELECT_DC_TARGETING_GALLERY_WHERE_ID, context, arrayOf(gallery.galleryID))
        val datas = ArrayList<GalleryMeta>()

        while(cursor.moveToNext()) {
            datas.add(
                GalleryMeta(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
        }

        return datas
    }
}