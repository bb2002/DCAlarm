package kr.saintdev.dcalarm.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_keyword.*
import kr.saintdev.dcalarm.R
import kr.saintdev.dcalarm.modules.km.Keyword
import kr.saintdev.dcalarm.modules.km.KeywordManager
import kr.saintdev.dcalarm.views.alert.OnAlertConfirmClickListener
import kr.saintdev.dcalarm.views.alert.openAlert
import kr.saintdev.dcalarm.views.alert.openConfirm

class KeywordActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword)

        this.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        keyword_list.adapter = this.adapter
        updateAdapter()

        keyword_add_button.setOnClickListener {
            val keywordText = keyword_editor.text.toString()
            if(keywordText.isEmpty()) {
                R.string.keyword_empty.openAlert(this, "Error")
            } else {
                val kManager = KeywordManager.getInstance(this)
                val newKeyword = Keyword(0, keywordText)

                if(kManager.isExsit(newKeyword)) {
                    R.string.keyword_exsit.openAlert(this, "Warning")
                } else {
                    kManager.insertKeyword(Keyword(0, keywordText))
                    updateAdapter()
                }

                keyword_editor.setText("")
            }
        }

        keyword_list.setOnItemClickListener { _, _, pos, _ ->
            R.string.keyword_delete_q.openConfirm(this@KeywordActivity, "Notification", R.string.delete_positive, R.string.cancel, object : OnAlertConfirmClickListener {
                override fun onPositive() {
                    val kManager = KeywordManager.getInstance(this@KeywordActivity)
                    val keyword = kManager.readAllKeywords()[pos]
                    kManager.removeKeyword(keyword.id)

                    updateAdapter()
                }

                override fun onNegative() {
                    updateAdapter()
                }
            })
        }
    }

    private fun updateAdapter() {
        val kManager = KeywordManager.getInstance(this)
        val keywords = kManager.readAllKeywords()

        this.adapter.clear()

        keywords.forEach {
            this.adapter.add(it.keyword)
        }

        this.adapter.notifyDataSetChanged()
    }
}
