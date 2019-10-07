package kr.saintdev.dcalarm.views.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_gallert_id.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.database.DatabaseManager
import kr.saintdev.dcalarm.modules.database.GalleryMetaDatabaseFunc
import kr.saintdev.dcalarm.modules.database.Insert
import kr.saintdev.dcalarm.modules.parser.DCWebParser
import kr.saintdev.dcalarm.modules.parser.DC_GALL_URL
import kr.saintdev.dcalarm.modules.parser.GalleryMeta
import kr.saintdev.dcalarm.modules.parser.PostMeta
import kr.saintdev.dcalarm.views.alert.openAlert
import kr.saintdev.dcalarm.views.alert.openProgress

class GalleryIDActivity : AppCompatActivity() {
    private var progressDialogInstance: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallert_id)

        add_gallery_save.setOnClickListener {
            val gallID = add_gallery_ideditor.text.toString()
            val isMinerGallery = isMGallery.isChecked
            val url = (DC_GALL_URL          // 기본 디씨 갤 URL
            + (if(isMinerGallery) { "/mgallery/board/lists/" } else { "/board/lists/" })      // 마이너 갤러리의 경우 처리
            + "?id=" + gallID)  // URL 완성

            // URL 에 대한 유효성 검사
            val parser = DCWebParser.getInstance()
            parser.startMetaDataParsing(url, onParseCompleteListener)

            this.progressDialogInstance = R.string.execute_isvalid.openProgress(this)
        }
    }

    private val onParseCompleteListener =
        object : DCWebParser.OnDCGalleryMetaParsedListener {
            override fun onSuccess(meta: GalleryMeta) {
                progressDialogInstance?.dismiss()

                val dbm = DatabaseManager.getInstance()
                val isTargetingGallery = GalleryMetaDatabaseFunc.read(this@GalleryIDActivity, meta).isEmpty()

                if(isTargetingGallery) {
                    meta.Insert(dbm, this@GalleryIDActivity)

                    // Open activity.
                    startActivity(Intent(this@GalleryIDActivity, GalleryListActivity::class.java))
                    finish()
                } else {
                    openTargetedGallery()
                }
            }

            override fun onFailed() {
                progressDialogInstance?.dismiss()
                R.string.execute_is_unvalid.openAlert(this@GalleryIDActivity, "Error")
            }

            // 이미 타겟팅 중인 갤러리 입니다.
            fun openTargetedGallery() {
                R.string.targeted_gallery.openAlert(this@GalleryIDActivity, "WARN")
            }
        }
}
