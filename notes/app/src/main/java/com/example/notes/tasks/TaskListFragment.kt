package com.example.notes.tasks


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.notes.R


class TaskListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var touchDelegate: TouchActionDelegate
    private lateinit var contentView: TaskListView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
            if (it is TouchActionDelegate) {
                touchDelegate = it
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false).apply {
            contentView = this as TaskListView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setContentView()
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.loadData()
    }

    private fun setContentView() {
        contentView.initView(touchDelegate, taskViewModel)
    }

    private fun bindViewModel() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        taskViewModel.taskLiveData.observe(this, Observer { taskList ->
            contentView.updateList(taskList)
        })

        taskViewModel.stateChangedLiveData.observe(this, Observer {itemState ->

            when(itemState){
                is TaskViewModel.ItemState.ItemUpdated -> contentView.updateItem(itemState.newTask, itemState.itemInList, itemState.itemInView)
                is TaskViewModel.ItemState.ItemDeleted -> contentView.deleteItem(itemState.itemInList,itemState.itemInView)
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TaskListFragment()
    }

    interface TouchActionDelegate {
        fun onAddButtonClicked(value: String)
    }
}
