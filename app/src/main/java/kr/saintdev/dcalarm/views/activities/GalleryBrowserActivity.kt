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
import kr.saintdev.dcalarm.modules.parser.DCWebParser
import kr.saintdev.dcalarm.modules.parser.DC_GALL_URL
import kr.saintdev.dcalarm.modules.parser.PostMeta

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
            parser.ParseGallery(url, ParserResultListener())
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

    private inner class ParserResultListener : DCWebParser.OnDCGalleryParsedListener {
        override fun onSuccess(document: ArrayList<PostMeta>) {
            Toast.makeText(applicationContext, "${document.size} 개로, 작동함!", Toast.LENGTH_SHORT).show()
        }

        override fun onFailed() {
            Toast.makeText(applicationContext, "안됨!", Toast.LENGTH_SHORT).show()

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
