package kr.saintdev.dcalarm.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_gallery_browser.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.DateUtilFunctions
import kr.saintdev.dcalarm.modules.database.DatabaseManager
import kr.saintdev.dcalarm.modules.database.SQLQueries
import kr.saintdev.dcalarm.modules.parser.DCWebParser
import kr.saintdev.dcalarm.modules.parser.DC_GALL_URL
import kr.saintdev.dcalarm.modules.parser.GalleryMeta
import kr.saintdev.dcalarm.modules.parser.PostMeta
import kr.saintdev.dcalarm.views.alert.openAlert

class GalleryBrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_browser)

        browser.settings.javaScriptEnabled = true
        browser.webViewClient = DCWebViewClient()
        browser.webChromeClient = WebChromeClient()
        browser.loadUrl(DC_GALL_URL)

        page_save.setOnClickListener {
            val url = browser.url.toString()

            // 해당 URL 의 유효성을 확인 한다.
            val parser = DCWebParser.getInstance()
            parser.startMetaDataParsing(url, MetaParserResultListener())
        }

        exit.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(browser.canGoBack()) {
            browser.goBack()
        } else {
            finish()
        }
    }

    private inner class MetaParserResultListener : DCWebParser.OnDCGalleryMetaParsedListener {
        override fun onSuccess(meta: GalleryMeta) {
            val database = DatabaseManager.getInstance()

            val pStmt = database.makeInsertQuery(SQLQueries.INSERT_DC_TARGETING_GALLERY, this@GalleryBrowserActivity)
            pStmt.bindString(1, meta.galleryName)
            pStmt.bindString(2, meta.galleryID)
            pStmt.bindString(3, DateUtilFunctions.getNowToString())
            pStmt.execute()

            Toast.makeText(this@GalleryBrowserActivity, R.string.execute_succ, Toast.LENGTH_SHORT).show();
        }

        override fun onFailed() {
            // 오류 발생시 경고창 열기
            R.string.execute_unvalid_err.openAlert(this@GalleryBrowserActivity, resources.getString(R.string.execute_isvalid_err))
        }
    }

    private inner class DCWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url.toString()
            view?.loadUrl(url)
            return true
        }
    }
}
