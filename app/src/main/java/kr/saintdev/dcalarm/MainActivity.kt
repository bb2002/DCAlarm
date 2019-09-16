package kr.saintdev.dcalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.saintdev.dcalarm.modules.parser.DCWebParser
import kr.saintdev.dcalarm.modules.parser.PostMeta

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val parser = DCWebParser.getInstance()
        parser.ParseGallery("http://gall.dcinside.com/mgallery/board/lists/?id=kizunaai", object : DCWebParser.OnDCGalleryParsedListener {
            override fun onSuccess(document: ArrayList<PostMeta>) {

            }

            override fun onFailed(reason: String) {
            }
        })
    }
}
