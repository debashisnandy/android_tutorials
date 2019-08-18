package com.example.doga.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs:DogBreed): List<Long>

    @Query("SELECT * FROM dogbreed WHERE uuid= :dogId")
    suspend fun getAllDogs(dogId:Int):DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()
}