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
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.database.GalleryMetaDatabaseFunc
import kr.saintdev.dcalarm.modules.km.KeywordManager
import kr.saintdev.dcalarm.modules.notifiarm.NotificationAlarmManager
import kr.saintdev.dcalarm.modules.parser.DCWebParser
import kr.saintdev.dcalarm.modules.parser.NotificationAlarmMeta
import kr.saintdev.dcalarm.modules.parser.PostMeta
import java.util.*

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

            val dcParser = DCWebParser.getInstance()
            val notifiAlarmManager = NotificationAlarmManager.getInstance(applicationContext)

            while(true) {
                val trackingGalleries = GalleryMetaDatabaseFunc.readAll(applicationContext)     // 갤러리 목록을 가져온다.

                if(trackingGalleries.isNotEmpty()) {
                    for(gal in trackingGalleries) {
                        // 갤러리에 대해 파싱을 시도 한다.
                        val posts = dcParser.parsePostFromDC(resources.getString(R.string.gallery_default_url) + gal.galleryID)

                        for(post in posts) {
                            if(isNotifiablePost(post)) {
                                // 이 포스트는 알릴만한 가치가 있다.
                                notifiAlarmManager.addNotificationAlarm(NotificationAlarmMeta(post, Date()))
                                sendNotificationWithNewPost(post)
                            }
                        }
                    }
                }

                sleep(1000)
            }

            isParserServiceRunning = false

        }

        fun isNotifiablePost(post: PostMeta) : Boolean {
            val keywords = KeywordManager.getInstance(applicationContext).readAllKeywords()
            val notifiAlarms = NotificationAlarmManager.getInstance(applicationContext).getAllNotifiAlarms()

            // Check Keyword Match
            var isKeywordMatch = false
            for(it in keywords) {
                if(post.title.contains(it.keyword)) {
                    isKeywordMatch = true
                    break
                }
            }

            if(!isKeywordMatch) {
                // Keyword Doesn't match.
                return false
            }

            // Check notification
            notifiAlarms.forEach {
                if(it.post.uuid == post.uuid && it.post.writer == post.writer) {
                    return false
                }
            }

            return true
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

    private fun sendNotificationWithNewPost(newPost: PostMeta) {
        val channelID = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("dc_service", "DCAlarmService")
        } else {
            ""
        }

        val notifiBuilder = NotificationCompat.Builder(this, channelID)
        val notification = notifiBuilder
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(newPost.title)
            .setContentText("${newPost.writer} 님이 작성했습니다.")
            .setAutoCancel(true)
            .build()

        val notifiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifiManager.notify(1, notification)

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