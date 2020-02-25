package com.marschen.architectureexample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.marschen.architectureexample.DataRepository
import com.marschen.architectureexample.db.Drama

class DramaViewModel(private val repository: DataRepository) :
    ViewModel() {
    private var searchQuery: MutableLiveData<String> = MutableLiveData()


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