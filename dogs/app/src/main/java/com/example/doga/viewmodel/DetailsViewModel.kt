package com.example.doga.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doga.model.DogBreed

class DetailsViewModel : ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){
        val dog1 = DogBreed("1","Corgi", "15 Years", "breedGroup","breadFor","temperament","")
        dogLiveData.value = dog1
    }
}