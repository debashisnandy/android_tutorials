package com.example.doga.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val DATABASE_VERSION = 1
const val DB_NAME = "local-db"


@Database(version = DATABASE_VERSION, entities = [DogBreed::class])
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao():DogDao

    companion object{
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()


        operator fun invoke(context:Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context:Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            DB_NAME
        ).build()
    }

}
