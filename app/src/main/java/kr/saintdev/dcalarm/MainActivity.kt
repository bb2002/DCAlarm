package kr.saintdev.dcalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.saintdev.dcalarm.modules.parser.DCWebParser
import kr.saintdev.dcalarm.modules.parser.PostMeta

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
