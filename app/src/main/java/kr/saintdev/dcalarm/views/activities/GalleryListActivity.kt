package kr.saintdev.dcalarm.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_gallery_list.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.database.GalleryMetaDatabaseFunc

class GalleryListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_list)

        val targetingGalleries = GalleryMetaDatabaseFunc.readAll(this)
//        val galleryListAdapter = ArrayAdapter<String>(this)


    }
}
