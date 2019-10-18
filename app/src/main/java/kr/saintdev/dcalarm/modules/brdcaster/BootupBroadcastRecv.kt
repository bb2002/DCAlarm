package kr.saintdev.dcalarm.modules.brdcaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kr.saintdev.dcalarm.modules.services.DCAlarmService

class BootupBroadcastRecv : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Start service.
        context?.startService(Intent(context, DCAlarmService::class.java))
    }
}