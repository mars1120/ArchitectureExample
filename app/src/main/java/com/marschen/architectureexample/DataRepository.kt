package com.marschen.architectureexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.marschen.architectureexample.api.DramaApi
import com.marschen.architectureexample.db.Drama
import com.marschen.architectureexample.db.DramaRoomDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository handling the work with products and comments.
 */
class DataRepository(private val mDatabase: DramaRoomDatabase) {


    companion object {
        private var sInstance: DataRepository? = null
        fun getInstance(database: DramaRoomDatabase): DataRepository {
            if (sInstance == null) {
                synchronized(DataRepository::class.java) {
                    if (sInstance == null) {
                        sInstance = DataRepository(database)
                    }
                }
            }
            return sInstance!!
        }
    }

    private val mObservableDramas: MediatorLiveData<List<Drama>> = MediatorLiveData()

    init {
        mObservableDramas.addSource(
            mDatabase.dramaDao().getAll()
        ) { dramaEntities: List<Drama> ->
            mObservableDramas.postValue(
                dramaEntities
            )
        }
    }

    fun callDramaApi(failCallback: (() -> Unit)?) {
        DramaApi.create().getDramaSample().enqueue(object : Callback<DramaApi.ListingResponse> {
            override fun onFailure(call: Call<DramaApi.ListingResponse>, t: Throwable) {
                failCallback?.invoke()
            }

            override fun onResponse(
                call: Call<DramaApi.ListingResponse>,
                response: Response<DramaApi.ListingResponse>
            ) {
                AppExecutors().diskIO().execute {
                    replaceAll(response.body()!!.data)
                }
            }

        })
    }

    fun getAllDramas(): LiveData<List<Drama>> {
        return mObservableDramas
    }

//    fun insertAll(dramas: List<Drama>) {
//        mDatabase.dramaDao().insert(dramas)
//    }

    fun replaceAll(dramas: List<Drama>) {
        deleteAll()
        mDatabase.dramaDao().insert(dramas)
    }


    fun searchDramasByName(query: String): LiveData<List<Drama>> {
        return mDatabase.dramaDao().searchByName(query)
    }

    private fun deleteAll() {
        mDatabase.dramaDao().deleteAll()
    }
}