package kr.saintdev.dcalarm.modules.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kr.saintdev.dcalarm.R

class DCAlarmService : Service() {
    override fun onCreate() {
        super.onCreate()
        val builder = NotificationCompat.Builder(this, "1234") // channel ID
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("Test")
        //.setContentIntent(pendingIntent)
        startForeground(1, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}