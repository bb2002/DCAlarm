package kr.saintdev.dcalarm.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.notifiarm.NotificationAlarmManager
import kr.saintdev.dcalarm.modules.services.DCAlarmService
import kr.saintdev.dcalarm.modules.sharedprep.SettingsFunction
import kr.saintdev.dcalarm.views.alert.OnAlertConfirmClickListener
import kr.saintdev.dcalarm.views.alert.openConfirm
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set OnClick Listener
        dc_galladd_fromid.setOnClickListener(addGalleryListener)
        dc_galladd_fromweb.setOnClickListener(addGalleryListener)
        dc_settings_gallery.setOnClickListener(addGalleryListener)
        dc_settings_keyword.setOnClickListener(addGalleryListener)
        notified_alarm_button.setOnClickListener(addGalleryListener)
        notified_alarm_clear_button.setOnClickListener(addGalleryListener)

        // Time Change Listener
        check_delay_time.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    val time = p0?.toString()?.toInt()
                    if (time != null) {
                        SettingsFunction.setParseingServiceDelay(this@MainActivity, time)
                    }
                } catch(ex: Exception) {}
            }
        })

        use_data_mode_switch.setOnCheckedChangeListener {
            _, bool -> SettingsFunction.setWorkOnDataMode(this, bool)
        }

        // Start service.
        startService(Intent(this, DCAlarmService::class.java))
    }

    override fun onResume() {
        super.onResume()

        check_delay_time.setText(SettingsFunction.getParseingServiceDelay(this).toString())
        use_data_mode_switch.isChecked = SettingsFunction.isWorkOnDataMode(this)
    }

    private val addGalleryListener = View.OnClickListener {
        when(it.id) {
            R.id.dc_galladd_fromid -> startActivity(Intent(this, GalleryIDActivity::class.java))
            R.id.dc_galladd_fromweb -> startActivity(Intent(this, GalleryBrowserActivity::class.java))
            R.id.dc_settings_gallery -> startActivity(Intent(this, GalleryListActivity::class.java))
            R.id.dc_settings_keyword -> startActivity(Intent(this, KeywordActivity::class.java))
            R.id.notified_alarm_button -> startActivity(Intent(this, NotifiedAlarmActivity::class.java))
            R.id.notified_alarm_clear_button -> alarmClearButtonClick()
        }
    }

    private fun alarmClearButtonClick() {
        R.string.notified_alarm_clear_check.openConfirm(this, "안내", R.string.ok, R.string.cancel, object : OnAlertConfirmClickListener {
            override fun onPositive() {
                NotificationAlarmManager.getInstance(this@MainActivity).removeAllNotificationAlarm()
                Toast.makeText(this@MainActivity, R.string.notified_alarm_clear_ok, Toast.LENGTH_SHORT).show()
            }

            override fun onNegative() {
            }
        })
    }
}
