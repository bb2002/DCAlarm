package kr.saintdev.dcalarm.modules.parser

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import kr.saintdev.dcalarm.modules.DateUtilFunctions
import org.jsoup.Jsoup
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.ArrayList

/**
 * DC INSIDE WEB PARSER CLASS.
 * HOW TO USE
 *
 *
val parser = DCWebParser.getInstance()
parser.ParseGallery("http://gall.dcinside.com/mgallery/board/lists/?id=bang_dream", object : DCWebParser.OnDCGalleryParsedListener {
override fun onSuccess(posts: ArrayList<PostMeta>) {

}

override fun onFailed() {
}
})

 *
 */

class DCWebParser {
    companion object {
        private var ins: DCWebParser? = null

        fun getInstance(): DCWebParser {
            if(ins == null) ins = DCWebParser()

            return ins!!
        }
    }

    /**
     * @Date 09.16 2019
     * 갤러리 파싱을 마쳤을 때 호출될 콜백 리스너
     */
    interface OnDCGalleryParsedListener {
        fun onSuccess(document: ArrayList<PostMeta>)
        fun onFailed()
    }

    /**
     * @Date 09.26 2019
     * 갤러리의 메타 정보를 파싱 하여 가져오는 콜백 리스너
     */
    interface OnDCGalleryMetaParsedListener {
        fun onSuccess(meta: GalleryMeta)
        fun onFailed()
    }

    /**
     * @Date 09.16 20190
     * IGNORE SSL CERT.
     */
    private fun socketFactory() : SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            return sslContext.socketFactory
        } catch(ex: Exception) {
            throw ex
        }
    }

    private inner class ParseAsyncTask(val callback: OnDCGalleryParsedListener) : AsyncTask<String, Void, ArrayList<PostMeta>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg url: String?): ArrayList<PostMeta>? {
            val postMetaArray = arrayListOf<PostMeta>()

            try {
                val document = Jsoup.connect(url[0]).sslSocketFactory(socketFactory()).get()
                val posts = document.select("tr.us-post")

                for(i in 0 until posts.size) {
                    val aPost = posts[i]

                    // Make Post Struct.
                    val postMeta = PostMeta(
                        title = aPost.getElementsByClass("gall_tit")[0].getElementsByTag("a")[0].text(),          // title
                        uuid = aPost.getElementsByClass("gall_num").text(),     // uuid
                        url = DC_GALL_URL + aPost.getElementsByClass("gall_tit")[0].getElementsByTag("a")[0].attr("href"),
                        writer = aPost.getElementsByClass("ub-writer").attr("data-nick"),
                        date = DateUtilFunctions.stringToDate(aPost.getElementsByClass("gall_date").attr("title")),
                        viewCount = aPost.getElementsByClass("gall_count").text().toInt()
                    )
                    // Add new post
                    postMetaArray.add(postMeta)
                }
            } catch(ex: Exception) {
                ex.printStackTrace()
                postMetaArray.clear()
            }

            return postMetaArray
        }

        override fun onPostExecute(result: ArrayList<PostMeta>?) {
            super.onPostExecute(result)

            if(result != null && result.size != 0) {
                callback.onSuccess(result)
            } else {
                callback.onFailed()
            }
        }
    }

    private inner class ParseGalleryMetaAsyncTask(val callback: OnDCGalleryMetaParsedListener) : AsyncTask<String, Void, GalleryMeta?>() {
        override fun doInBackground(vararg url: String?): GalleryMeta? {
            try {
                val document = Jsoup.connect(url[0]).sslSocketFactory(socketFactory()).get()
                val urlObj = Uri.parse(url[0])

                // get gallery ID
                var galleryID = urlObj.getQueryParameter("id")
                if(galleryID == null) {
                    val seg = urlObj.pathSegments
                    galleryID = seg[seg.size - 1]
                }

                val galleryTitle = document.select("header .page_head .fl h2 a").text()

                if(galleryTitle == null || galleryID == null) {
                    throw java.lang.Exception("Parser is not working.")
                }

                return GalleryMeta(galleryTitle, galleryID, DateUtilFunctions.getNowToString())
            } catch(ex: Exception) {
                ex.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: GalleryMeta?) {
            if(result == null) {
                callback.onFailed()
            } else {
                callback.onSuccess(result)
            }
        }
    }

    /**
     * @Date 09.16 2019
     * 파싱 작업을 수행 한다.
     */
    fun startParsing(targetURL: String, callback: OnDCGalleryParsedListener) {
        val task = ParseAsyncTask(callback)
        task.execute(targetURL)
    }

    /**
     * @Date 09.30 2019
     * 갤러리의 메타 데이터 파싱을 수행 한다.
     */
    fun startMetaDataParsing(targetURL: String, callback: OnDCGalleryMetaParsedListener) {
        val task = ParseGalleryMetaAsyncTask(callback)
        task.execute(targetURL)
    }
}