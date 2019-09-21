package kr.saintdev.dcalarm.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kr.saintdev.dcalarm.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set OnClick Listener
        dc_galladd_fromid.setOnClickListener(addGalleryListener)
        dc_galladd_fromweb.setOnClickListener(addGalleryListener)
    }

    private val addGalleryListener = View.OnClickListener {
        when(it.id) {
            R.id.dc_galladd_fromid -> startActivity(Intent(this, GallertIDActivity::class.java))
            R.id.dc_galladd_fromweb -> startActivity(Intent(this, GalleryBrowserActivity::class.java))
        }
    }
}
