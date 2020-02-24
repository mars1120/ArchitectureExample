package com.marschen.architectureexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.marschen.architectureexample.api.DramaApi
import com.marschen.architectureexample.db.Drama
import com.marschen.architectureexample.db.DramaRoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.sonet.picktime.ui.eventlist.adapter.DramasAdapter


class MainActivity : AppCompatActivity() {
    val repository: DataRepository by lazy {
        DataRepository.getInstance(DramaRoomDatabase.getDatabase(this))
    }
    val searchQuery = MutableLiveData<String>().apply { postValue(null) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_recycler.apply {
            adapter = DramasAdapter()
        }

        var mDramas: LiveData<List<Drama>> = Transformations.switchMap(searchQuery) { query ->
            if (query == null) {
                repository.allDramas
            } else {
                repository.searchDramasByName(query)
            }
        }
        mDramas.observe(this, Observer {
            (main_recycler.adapter as DramasAdapter).update(it)
            if (main_refresh.isRefreshing) main_refresh.isRefreshing = false
        })
        callDramaApi()
        main_refresh.setOnRefreshListener {
            callDramaApi()
        }
    }

    fun callDramaApi() {
        DramaApi.create().getDramaSample().enqueue(object : Callback<DramaApi.ListingResponse> {
            override fun onFailure(call: Call<DramaApi.ListingResponse>, t: Throwable) {
                if (main_refresh.isRefreshing) main_refresh.isRefreshing = false
            }

            override fun onResponse(
                call: Call<DramaApi.ListingResponse>,
                response: Response<DramaApi.ListingResponse>
            ) {
                AppExecutors().diskIO().execute {
                    repository.replaceAll(response.body()!!.data)
                }

            }

        })
    }

}
