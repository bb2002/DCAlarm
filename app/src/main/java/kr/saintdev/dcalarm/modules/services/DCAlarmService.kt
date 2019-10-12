package kr.saintdev.dcalarm.modules.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kr.saintdev.dcalarm.R

class DCAlarmService : Service() {
    private var isParserServiceRunning = false
    private var parserThread: DCParserThread? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()

        if(!isParserServiceRunning) {
            parserThread = DCParserThread()
            parserThread!!.start()
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    inner class DCParserThread : Thread() {
        override fun run() {
            isParserServiceRunning = true

            while(true) {
                break
            }

            isParserServiceRunning = false

        }
    }

    /**
     * @Date 10.12 2019
     * StartForegroundService
     */
    private fun startForegroundService() {
        val channelID = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("dc_service", "DCAlarmService")
        } else {
            ""
        }

        val notifiBuilder = NotificationCompat.Builder(this, channelID)
        val notification = notifiBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(resources.getString(R.string.notification_content))
            .build()

        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String) : String {
        val chan = NotificationChannel(id,
            name, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return id
    }
}