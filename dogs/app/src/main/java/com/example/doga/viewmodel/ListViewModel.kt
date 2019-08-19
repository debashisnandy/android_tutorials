package com.example.doga.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doga.Util.SharedPreferenceHelper
import com.example.doga.model.DogBreed
import com.example.doga.model.DogDatabase
import com.example.doga.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application): BaseViewModel(application) {

    private val prefHelper = SharedPreferenceHelper(getApplication())
    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    fun refresh(){
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime!=0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        }else {
            fetchFromRemote()
        }

    }



    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogList = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogRetrived(dogList)
            Toast.makeText(getApplication(),"Data retrieved from database",Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
            dogService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogsList: List<DogBreed>) {
                        storeDogsLocally(dogsList)
                        Toast.makeText(getApplication(),"Data retrieved from remote",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        dogLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dogRetrived(dogsList: List<DogBreed>){
        dogs.value = dogsList
        dogLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(dogsList: List<DogBreed>){
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*dogsList.toTypedArray())
            for (i in 0 until dogsList.size){
                dogsList[i].uuid = result[i].toInt()
            }
            dogRetrived(dogsList)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}