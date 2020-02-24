package com.marschen.architectureexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.marschen.architectureexample.db.Drama
import com.marschen.architectureexample.db.DramaRoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import tw.sonet.picktime.ui.eventlist.adapter.DramasAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_recycler.apply {
            adapter = DramasAdapter()
        }
        var repository: DataRepository = DataRepository.getInstance(DramaRoomDatabase.getDatabase(this))
//        AppExecutors().diskIO().execute {
//            repository.replaceAll(drama.data!!)
//        }
        repository.allDramas.observe(this, Observer {
            (main_recycler.adapter as DramasAdapter).update(it)
        })
    }

    class DremaObj {
        var data: List<Drama>? = null
    }
}
