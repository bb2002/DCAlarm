package kr.saintdev.dcalarm.modules.notifiarm

import android.content.Context
import kr.saintdev.dcalarm.modules.DEFAULT_DATE_STRING
import kr.saintdev.dcalarm.modules.database.DatabaseManager
import kr.saintdev.dcalarm.modules.database.SQLQueries
import kr.saintdev.dcalarm.modules.parser.NotificationAlarmMeta
import kr.saintdev.dcalarm.modules.parser.PostMeta
import kr.saintdev.dcalarm.modules.toDate
import kr.saintdev.dcalarm.modules.toFormatString

class NotificationAlarmManager private constructor(private val context: Context) {
    companion object {
        private var ins: NotificationAlarmManager? = null

        fun getInstance(context: Context) : NotificationAlarmManager {
            if(ins == null) {
                ins = NotificationAlarmManager(context)
            }

            return ins!!
        }
    }

    private val databaseManager: DatabaseManager = DatabaseManager.getInstance()

    /**
     * @Date 10.17 2019
     * get all notification alarms
     */
    fun getAllNotifiAlarms() : ArrayList<NotificationAlarmMeta> {
        val cursor = databaseManager.executeQuery(SQLQueries.SELECT_DC_NOTIFIED_ALARMS, this.context, null)
        val notifications = ArrayList<NotificationAlarmMeta>()

        while(cursor.moveToNext()) {
            val tmp = NotificationAlarmMeta(
                PostMeta(
                    cursor.getString(1),            // UUID
                    cursor.getString(2),            // URL
                    cursor.getString(3),            // Title
                    cursor.getString(4),            // Writer
                    cursor.getString(5).toDate(),    // Date
                    -1), cursor.getString(6).toDate()
            )
            notifications.add(tmp)
        }

        return notifications
    }

    /**
     * @Date 10.17 2019
     * add new notification alarm
     */
    fun addNotificationAlarm(item: NotificationAlarmMeta) {
        val stmt = databaseManager.makeInsertQuery(SQLQueries.INSERT_DC_NOTIFIED_ALARM, this.context)
        stmt.bindString(1, item.post.uuid)
        stmt.bindString(2, item.post.url)
        stmt.bindString(3, item.post.title)
        stmt.bindString(4, item.post.writer)
        stmt.bindString(5, item.post.date?.toFormatString() ?: DEFAULT_DATE_STRING)
        stmt.bindString(6, item.ndate.toFormatString())

        stmt.execute()
    }

    /**
     * @Date 10.18 2019
     * Clear all notification alarms.
     */
    fun removeAllNotificationAlarm() {
        databaseManager.makeReadQuery(SQLQueries.DELETE_DC_NOTIFIED_ALARMS, this.context).execute()
    }
}