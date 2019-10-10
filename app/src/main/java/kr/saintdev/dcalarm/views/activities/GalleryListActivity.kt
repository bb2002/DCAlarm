package kr.saintdev.dcalarm.views.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_gallery_list.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.database.DatabaseManager
import kr.saintdev.dcalarm.modules.database.GalleryMetaDatabaseFunc
import kr.saintdev.dcalarm.modules.database.removeFromDB
import kr.saintdev.dcalarm.modules.parser.GalleryMeta
import kr.saintdev.dcalarm.views.alert.OnAlertConfirmClickListener
import kr.saintdev.dcalarm.views.alert.openAlert
import kr.saintdev.dcalarm.views.alert.openConfirm

class GalleryListActivity : AppCompatActivity() {
    private lateinit var galleryListAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_list)

        this.galleryListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        updateAdapter()

        // Set Adapter.
        galery_list.adapter = this.galleryListAdapter
        galery_list.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos, _ ->
                R.string.gallery_list_delete_q.openConfirm(this@GalleryListActivity, "Notification", R.string.delete_positive, R.string.cancel, object : OnAlertConfirmClickListener {
                    override fun onPositive() {
                        val delGallery = GalleryMetaDatabaseFunc.readAll(this@GalleryListActivity)[pos]
                        val dbm = DatabaseManager.getInstance()
                        delGallery.removeFromDB(dbm, this@GalleryListActivity)

                        updateAdapter()
                    }

                    override fun onNegative() {
                        updateAdapter()
                    }
                })
            }
    }

    private fun updateAdapter() {
        val targetingGalleries = GalleryMetaDatabaseFunc.readAll(this)
        this.galleryListAdapter.clear()

        for(item in targetingGalleries) {
            galleryListAdapter.add(item.galleryName)
        }

        this.galleryListAdapter.notifyDataSetChanged()
    }
}
