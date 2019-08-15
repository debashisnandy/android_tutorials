package com.example.doga.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doga.model.DogBreed

class ListViewModel: ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    fun refresh(){
        val dog1 = DogBreed("1","Corgi", "15 Years", "breedGroup","breadFor","temperament","")
        val dog2 = DogBreed("2","Labrador", "15 Years", "breedGroup","breadFor","temperament","")
        val dog3 = DogBreed("3","Rotwailer", "15 Years", "breedGroup","breadFor","temperament","")

        val dogList = arrayListOf(dog1,dog2,dog3)

        dogs.value = dogList
        dogLoadError.value = false
        loading.value = false
    }
}