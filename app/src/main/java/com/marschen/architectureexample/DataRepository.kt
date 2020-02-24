package com.marschen.architectureexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.marschen.architectureexample.db.Drama
import com.marschen.architectureexample.db.DramaRoomDatabase

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

    val allDramas: LiveData<List<Drama>>
        get() = mObservableDramas

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