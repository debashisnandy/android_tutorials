package com.example.doga.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.doga.model.DogBreed
import com.example.doga.model.DogDatabase
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(id:Int){
        launch {
            val dogList = DogDatabase(getApplication()).dogDao().getDog(id)
            dogLiveData.value = dogList
        }
    }
}