package com.marschen.architectureexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.marschen.architectureexample.db.Drama
import kotlinx.android.synthetic.main.activity_drama_info.*


class DramaInfoActivity : AppCompatActivity() {
    companion object {
        private const val KEY_DRAMA_INFO = "drama_info"
        fun intentFor(context: Context, data: Drama?): Intent {

            val intent = Intent(context, DramaInfoActivity::class.java)
            intent.putExtra(KEY_DRAMA_INFO, data)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drama_info)
        var dramaInfo: Drama = intent.getParcelableExtra(KEY_DRAMA_INFO)
        bindInfo(dramaInfo)
    }

    fun bindInfo(drama: Drama) {
        Glide.with(this).load(drama.thumb).into(dramainfo_thumbnail)
        dramainfo_name.text = "名稱 : " + drama.name
        dramainfo_rating.text = "評分 : " +
                getString(R.string.score, "" + drama.rating)
        dramainfo_created_at.text = "出版日期 : " + drama.created_at
        dramainfo_total_views.text = "觀看次數 : " + drama.total_views
    }

}
