package com.example.notes.database

import androidx.room.*
import com.example.notes.models.Task
import com.example.notes.models.TaskEntity
import com.example.notes.models.Todo

@Dao
interface TaskDAO {

    @Insert
    fun insertTask(taskEntity: TaskEntity)

    @Insert
    fun insertTodo(todo: Todo)

    @Update
    fun updateTask(taskEntity: TaskEntity)

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun deleteTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun retriveTask(): MutableList<Task>
}