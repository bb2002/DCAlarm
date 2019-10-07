package kr.saintdev.dcalarm.views.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_gallery_list.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.database.GalleryMetaDatabaseFunc
import kr.saintdev.dcalarm.modules.parser.GalleryMeta

class GalleryListActivity : AppCompatActivity() {
    private lateinit var galleryListAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_list)

        val targetingGalleries = GalleryMetaDatabaseFunc.readAll(this)
        this.galleryListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        for(item in targetingGalleries) {
            galleryListAdapter.add(item.galleryName)
        }

        // Set Adapter.
        galery_list.adapter = this.galleryListAdapter
    }
}
