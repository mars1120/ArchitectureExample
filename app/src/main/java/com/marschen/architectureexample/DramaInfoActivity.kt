package com.marschen.architectureexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.marschen.architectureexample.db.Drama
import com.marschen.architectureexample.db.DramaRoomDatabase
import com.marschen.architectureexample.viewmodel.DramaViewModel
import kotlinx.android.synthetic.main.activity_drama_info.*


class DramaInfoActivity : AppCompatActivity() {
    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @NonNull
                override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
                    return DramaViewModel(
                        DataRepository.getInstance(DramaRoomDatabase.getDatabase(application))
                    ) as T
                }
            }
        ).get(DramaViewModel::class.java)
    }

    companion object {
        private const val KEY_DRAMA_ID = "key_drama_id"
        fun intentFor(context: Context, dramaID: Int): Intent {

            val intent = Intent(context, DramaInfoActivity::class.java)
            intent.putExtra(KEY_DRAMA_ID, dramaID)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drama_info)
        var dramaID: Int = intent.getIntExtra(KEY_DRAMA_ID, 0)
        mViewModel.searchByID(dramaID).observe(this, Observer {
            if (it != null) bindInfo(it)
        })
    }

    private fun bindInfo(drama: Drama) {
        Glide.with(this).load(drama.thumb).into(dramainfo_thumbnail)
        dramainfo_name.text = "名稱 : " + drama.name
        dramainfo_rating.text = "評分 : " +
                getString(R.string.score, "" + drama.rating)
        dramainfo_created_at.text = "出版日期 : " + drama.created_at
        dramainfo_total_views.text = "觀看次數 : " + drama.total_views
    }

}
