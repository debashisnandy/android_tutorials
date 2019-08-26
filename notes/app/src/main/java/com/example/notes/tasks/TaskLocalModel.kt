package com.example.notes.tasks

import com.example.notes.application.NoteApplication
import com.example.notes.database.RoomDatabaseClient
import com.example.notes.models.Task
import com.example.notes.models.Todo
import com.example.notes.notes.TIMEOUT_DURATION_MILLIS
import kotlinx.coroutines.*
import javax.inject.Inject

class TaskLocalModel @Inject constructor(): ITaskModel {

    private val databaseClient = RoomDatabaseClient.getInstance(NoteApplication.instance.applicationContext)

    private suspend fun performOperationWithTimeout(function: () -> Unit, callBack: SuccessCallBack) {

        val job = GlobalScope.async {
            try {
                withTimeout(TIMEOUT_DURATION_MILLIS) {
                    function.invoke()
                }

            } catch (e: Exception) {
                callBack.invoke(false)
            }
        }
        job.await()
        callBack.invoke(true)

    }

    override suspend fun addTask(task: Task, callBack: SuccessCallBack) {
        val masterJob = GlobalScope.async {

            try {
                databaseClient.taskDao().insertTask(task)
            } catch (e: Exception) {
                callBack.invoke(false)
            }

            addTodoJobs(task)
        }
        masterJob.await()
        callBack.invoke(true)
    }

    override suspend fun updateTask(task: Task, callBack: SuccessCallBack) {
        performOperationWithTimeout({databaseClient.taskDao().updateTask(task)},callBack)
    }

    private fun addTodoJobs(task:Task):Job = GlobalScope.async {
        task.todos.forEach {
            databaseClient.taskDao().insertTodo(it)
        }
    }



    override suspend fun updateTodo(todo: Todo, callBack: SuccessCallBack) {
        performOperationWithTimeout({databaseClient.taskDao().updateTodo(todo)},callBack)
    }

    override suspend fun deleteTask(task: Task, callBack: SuccessCallBack) {
        performOperationWithTimeout({databaseClient.taskDao().deleteTask(task)},callBack)
    }

    override fun retriveTask(callback: (List<Task>?) -> Unit){
        GlobalScope.launch {

            val job = async {
                withTimeoutOrNull(TIMEOUT_DURATION_MILLIS){
                    databaseClient.taskDao().retriveTask()
                }
            }
            callback.invoke(job.await())
        }
    }
}