package com.marschen.architectureexample.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.marschen.architectureexample.DataRepository
import com.marschen.architectureexample.db.Drama

class DramaViewModel(private val repository: DataRepository, application: Application) :
    AndroidViewModel(application) {
    private var dramasList: LiveData<List<Drama>>
    private var searchQuery: MutableLiveData<String> = MutableLiveData()


    init {
        dramasList = repository.getAllDramas()
    }

    fun getDramasForAdapter(): LiveData<List<Drama>> =
        Transformations.switchMap(searchQuery) { query ->
            if (query == null || query.equals("")) {
                repository.getAllDramas()
            } else {
                repository.searchDramasByName(query)
            }
        }

    fun searchDB(keyword: String) {
        searchQuery.postValue(keyword)
    }

    fun getSearchQuery(): LiveData<String> {
        return searchQuery
    }

    fun callDramaApi(failCallback: (() -> Unit)?) {
        repository.callDramaApi(failCallback)
    }
}