package com.example.notes.tasks

import com.example.notes.models.Task
import com.example.notes.models.Todo

typealias SuccessCallBack = (Boolean) -> Unit

interface ITaskModel {
    suspend fun addTask(task: Task, callBack: SuccessCallBack)
    suspend fun updateTodo(todo: Todo,callBack: SuccessCallBack)
    suspend fun updateTask(task:Task, callBack: SuccessCallBack)
    suspend fun deleteTask(task:Task, callBack: SuccessCallBack)
    fun retriveTask(callback: (List<Task>?) -> Unit)
}