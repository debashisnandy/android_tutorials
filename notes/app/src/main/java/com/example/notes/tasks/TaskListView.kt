package com.example.notes.tasks

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.models.Task
import kotlinx.android.synthetic.main.fragment_task_list.view.*

class TaskListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr
) {

    lateinit var adapter: TaskAdapter
    lateinit var touchActionDelegate: TaskListFragment.TouchActionDelegate
    lateinit var dataActionDelegate: TaskViewContract

    fun initView(taDelegate: TaskListFragment.TouchActionDelegate, daDelegate: TaskViewContract) {
        setDelegate(taDelegate, daDelegate)
        setUpView()
    }

    private fun setDelegate(taDelegate: TaskListFragment.TouchActionDelegate, daDelegate: TaskViewContract) {
        touchActionDelegate = taDelegate
        dataActionDelegate = daDelegate
    }

    private fun setUpView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TaskAdapter(
            touchActionDelegate = touchActionDelegate,
            dataActionDelegate = dataActionDelegate
        )

        recyclerView.adapter = adapter
    }

    fun updateList(list: List<Task>) {
        adapter.uiUpdate(list)
    }

    fun updateItem(newTask:Task, indexInList:Int, indexInView:Int){
        adapter.onItemUpdate(newTask,indexInList = indexInList,indexInView =  indexInView)
    }

    fun deleteItem(indexInList:Int, indexInView:Int){
        adapter.onItemDeleted(indexInList = indexInList, indexInView = indexInView)
    }
}