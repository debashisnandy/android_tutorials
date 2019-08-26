package com.example.notes.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notes.foundations.ApplicationScope
import com.example.notes.models.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.Toothpick
import javax.inject.Inject

class TaskViewModel : ViewModel(), TaskViewContract {

    @Inject
    lateinit var model: ITaskModel

    private val _taskLiveData: MutableLiveData<MutableList<Task>> = MutableLiveData()
    val taskLiveData: LiveData<MutableList<Task>> = _taskLiveData

    private val _stateChangeLiveData: MutableLiveData<ItemState> = MutableLiveData()
    val stateChangedLiveData: LiveData<ItemState> = _stateChangeLiveData

    init {
        Toothpick.inject(this,ApplicationScope.scope)
        loadData()
    }

    fun loadData(){
        model.retriveTask {
            it?.let {
                _taskLiveData.postValue(it.toMutableList())
            }
        }
    }

    override fun onTodoUpdate(taskIndex: Int, todoIndex: Int, isCompleted: Boolean) {
        GlobalScope.launch {
            _taskLiveData.value?.let {taskList->
                val todo = taskList[taskIndex].todos[todoIndex]
                todo.apply {
                    this.isComplete = isCompleted
                    this.taskId = taskList[taskIndex].uid
                }
                model.updateTodo(todo){success->
                    if (success){
                        _stateChangeLiveData.postValue(ItemState.ItemUpdated(
                            newTask = taskList[taskIndex],
                            il = taskIndex,
                            iv = taskIndex+1
                        ))
                    }

                }
            }
        }

    }

    override fun onTaskDeleted(taskIndex: Int) {
        GlobalScope.launch {
            _taskLiveData.value?.let {taskList ->
                model.deleteTask(taskList[taskIndex]){ success ->
                    if (success){
                        _stateChangeLiveData.postValue(
                            ItemState.ItemDeleted(
                                il = taskIndex,
                                iv = taskIndex + 1
                            )
                        )
                    }
                }
            }
        }

    }

    sealed class ItemState(val itemInList:Int,val itemInView: Int){
        class ItemUpdated(val newTask:Task, il:Int,iv:Int):ItemState(il,iv)
        class ItemDeleted(il: Int,iv: Int):ItemState(il,iv)
    }
}