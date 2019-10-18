package kr.saintdev.dcalarm.views.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_notified_alarm.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.notifiarm.NotificationAlarmManager
import kr.saintdev.dcalarm.modules.parser.NotificationAlarmMeta
import kr.saintdev.dcalarm.modules.toFormatString

class NotifiedAlarmActivity : AppCompatActivity() {
    lateinit var listAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notified_alarm)

        listAdapter = AlarmAdapter()
        alarm_list.adapter = listAdapter
        alarm_list.setOnItemClickListener {
            _, _, position, _ ->
            val intent = Intent()
            intent.data = Uri.parse(listAdapter.getItem(position).post.url)
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        }

    }

    inner class AlarmAdapter : BaseAdapter() {
        private val arrayItems = arrayListOf<NotificationAlarmMeta>()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView
                ?: LayoutInflater.from(applicationContext).inflate(android.R.layout.simple_list_item_2, parent, false)

            val titleView = view.findViewById<TextView>(android.R.id.text1)
            val writerView = view.findViewById<TextView>(android.R.id.text2)
            val alarmItem = getItem(position)

            val title =
                if(alarmItem.post.title.length > 20) alarmItem.post.title.slice(0 .. 19) + "..."
                else alarmItem.post.title

            titleView.text = title
            writerView.text = "${alarmItem.post.writer} 님, ${alarmItem.ndate?.toFormatString()}"
            return view
        }

        override fun getItem(p0: Int) = arrayItems[p0]

        override fun getItemId(p0: Int) = p0.toLong()

        override fun getCount() = arrayItems.size

        fun addItem(alarm: NotificationAlarmMeta) {
            this.arrayItems.add(alarm)
        }

        fun clear() {
            arrayItems.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshItems()
    }

    private fun refreshItems() {
        val notifiManager = NotificationAlarmManager.getInstance(this)
        val alarms = notifiManager.getAllNotifiAlarms()

        listAdapter.clear()
        alarms.forEach {
            listAdapter.addItem(it)
        }
    }
}
