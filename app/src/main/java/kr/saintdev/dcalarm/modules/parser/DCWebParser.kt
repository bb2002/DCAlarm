package kr.saintdev.dcalarm.modules.parser

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class DCWebParser {
    companion object {
        var ins: DCWebParser? = null

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
        fun onFailed(reason: String)
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

    private inner class ParseAsyncTask() : AsyncTask<String, Void, ArrayList<PostMeta>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg url: String?): ArrayList<PostMeta>? {
            try {
                val document = Jsoup.connect(url[0]).sslSocketFactory(socketFactory()).get()
                val posts = document.select("tr.us-post")

                for(i in 0 until posts.size) {
                    val aPost = posts[i]

                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val postDate = formatter.parse(aPost.getElementsByClass("gall_date").text())

                    // Make Post Struct.
                    val postMeta = PostMeta(
                        title = aPost.getElementsByTag("a").text(),             // title
                        uuid = aPost.getElementsByClass("gall_num").text(),    // uuid
                        url = aPost.getElementById("a").attr("href"),
                        writer = aPost.getElementsByClass("ub-writer").attr("data-nick"),
                        date = postDate,
                        viewCount = aPost.getElementsByClass("gall_count").text().toInt()
                    )

                    Log.e("Parse", postMeta.title)
                }
            } catch(ex: Exception) {
                ex.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: ArrayList<PostMeta>?) {
            super.onPostExecute(result)
        }
    }

    /**
     * @Date 09.16 2019
     * 파싱 작업을 수행 한다.
     */
    fun ParseGallery(targetURL: String, callback: OnDCGalleryParsedListener) {
        val task = ParseAsyncTask()
        task.execute(targetURL)
    }
}