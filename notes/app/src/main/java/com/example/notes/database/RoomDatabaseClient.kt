package com.example.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.models.Note
import com.example.notes.models.Tag
import com.example.notes.models.TaskEntity
import com.example.notes.models.Todo

const val DATABASE_VERSION = 1
const val DB_NAME = "local-db"

@Database(version = DATABASE_VERSION, entities = [TaskEntity::class, Todo::class, Tag::class, Note::class])
abstract class RoomDatabaseClient: RoomDatabase() {

    abstract fun noteDao():NoteDAO
    abstract fun taskDao():TaskDAO

    companion object{
        private var instance:RoomDatabaseClient? = null

        fun getInstance(context: Context):RoomDatabaseClient{
            if (instance == null){
                instance = createDatabase(context)
            }
            return instance!!
        }

        private fun createDatabase(context:Context):RoomDatabaseClient{

            return Room.databaseBuilder(context,RoomDatabaseClient::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}