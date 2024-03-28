package com.application.gosporttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.gosporttest.models.Repository
import com.application.gosporttest.models.RepositoryImp
import com.application.gosporttest.room.Category
import com.application.gosporttest.room.GoSport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoSportViewModel(private val repository: Repository): ViewModel() {
    private val _dataList = MutableLiveData<MutableList<GoSport>>()
    val dataList: LiveData<MutableList<GoSport>> = _dataList
    private var coroutine: Job? = null
    private var dataAll:String? = ""

    constructor() : this(RepositoryImp())

    private val _listCategories = MutableLiveData<List<Category>>()
    val listCategories: LiveData<List<Category>> = _listCategories

    fun startWork() {
        coroutine = CoroutineScope(Dispatchers.IO).launch {
            val internet = async {repository.checkInternet()}.await()
            if(internet){
                val urlCategories = "https://www.themealdb.com/api/json/v1/1/categories.php"
                val data  = async {repository.startRequestRetrofit(urlCategories)}.await()

                if (data!= null && data!= ""){
                    val l = repository.convertDataCategories(data)
                    withContext(Dispatchers.Main){
                        _listCategories.value = l
                    }
                    val urlAll = "https://themealdb.com/api/json/v1/1/search.php?s"
                    dataAll  = async {repository.startRequestRetrofit(urlAll)}.await()
                    if (dataAll!= null && dataAll!= ""){
                        repository.convertDataFull(dataAll)
                        val listFiltered = repository.filterDataFull(0)
                        withContext(Dispatchers.Main){
                            _dataList.value = listFiltered
                        }
                    }
                }

            }else{
                showNecessaryData(0)
            }
        }
    }

    fun showNecessaryData(index:Int){
        coroutine = CoroutineScope(Dispatchers.IO).launch {
            val listFiltered = repository.filterDataFull(index)
            withContext(Dispatchers.Main){
                _dataList.value = listFiltered
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        coroutine?.cancel()
    }
}