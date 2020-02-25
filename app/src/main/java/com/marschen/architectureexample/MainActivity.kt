package com.marschen.architectureexample

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marschen.architectureexample.db.DramaRoomDatabase
import com.marschen.architectureexample.viewmodel.DramaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import tw.sonet.picktime.ui.eventlist.adapter.DramasAdapter


class MainActivity : AppCompatActivity() {
    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @NonNull
                override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
                    return DramaViewModel(
                        DataRepository.getInstance(DramaRoomDatabase.getDatabase(application)),
                        application
                    ) as T
                }
            }
        ).get(DramaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_recycler.apply {
            adapter = DramasAdapter()
        }

        mViewModel.getDramasForAdapter().observe(this, Observer {
            (main_recycler.adapter as DramasAdapter).update(it)
            if (main_refresh.isRefreshing) main_refresh.isRefreshing = false
        })
        mViewModel.callDramaApi {
            main_refresh.isRefreshing = false
        }
        main_refresh.setOnRefreshListener {
            mViewModel.callDramaApi {
                main_refresh.isRefreshing = false
            }
        }
    }
}
