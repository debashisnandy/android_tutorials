package com.example.notes.create

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.notes.R
import com.example.notes.foundations.ApplicationScope
import com.example.notes.foundations.NullFieldChecker
import com.example.notes.foundations.StateChangeTextWatcher
import com.example.notes.models.Task
import com.example.notes.models.Todo
import com.example.notes.tasks.ITaskModel
import com.example.notes.tasks.TaskLocalModel
import com.example.notes.views.CreateTodoView
import kotlinx.android.synthetic.main.fragment_create_task.*
import kotlinx.android.synthetic.main.fragment_create_task.containerView
import kotlinx.android.synthetic.main.fragment_create_task.view.*
import kotlinx.android.synthetic.main.view_create_task.view.*
import kotlinx.android.synthetic.main.view_create_todo.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import toothpick.Toothpick
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MAX_TODO_COUNT = 5
private const val ARG_PARAM2 = "param2"



class CreateTaskFragment : Fragment() {


    @Inject
    lateinit var model:ITaskModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, ApplicationScope.scope)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTaskView.taskeditText.addTextChangedListener(object: StateChangeTextWatcher(){
            override fun afterTextChanged(s: Editable?) {

                if (!s.isNullOrEmpty() && previousVale.isNullOrEmpty()){
                    addTodoView()
                }
                super.afterTextChanged(s)
            }
        })
    }

    private fun isTaskEmpty():Boolean = createTaskView.taskeditText.editableText.isNullOrEmpty()


    fun saveTask(callback: (Boolean) -> Unit){
        GlobalScope.async {
            createTask()?.let {task->
                model.addTask(task){
                    callback.invoke(it)
                }
            }?:callback.invoke(false)
        }

    }

    fun createTask():Task?{
        if (!isTaskEmpty()){
            containerView.run {
                var taskField:String? = null
                val todoList:MutableList<Todo> = mutableListOf()
                for(i in 0 until this@run.childCount){

                    if (i==0){
                        taskField = containerView.taskeditText.editableText?.toString()
                    }else{

                        if (containerView.getChildAt(i).todoEditText.editableText?.toString() != ""){
                            todoList.add(
                                Todo(description =  containerView.getChildAt(i).todoEditText.editableText.toString())
                            )
                        }
                    }
                }
               return taskField?.let {
                   Task(it,todoList)
               }
            }
        }else{
            return null
        }
    }

    private fun addTodoView(){
        if (canAddTodos()){
            val view = (LayoutInflater.from(context).inflate(R.layout.view_create_todo,containerView,false) as CreateTodoView).apply {
                todoEditText.addTextChangedListener(object : StateChangeTextWatcher(){
                    override fun afterTextChanged(s: Editable?) {
                        if (!s.isNullOrEmpty() && previousVale.isNullOrEmpty()){
                            addTodoView()
                        } else if (!previousVale.isNullOrEmpty() && s.isNullOrEmpty()){
                            removeTodoView(this@apply)

                            if (containerView?.childCount == MAX_TODO_COUNT){
                                addTodoView()
                            }
                        }
                        super.afterTextChanged(s)
                    }
                })
            }

            containerView.addView(view)
        }

    }

    private fun removeTodoView(view: View){
        containerView.removeView(view)
    }

    private fun canAddTodos():Boolean = (containerView.childCount < MAX_TODO_COUNT +1) &&
            !(containerView.getChildAt(containerView.childCount-1) as NullFieldChecker).hasNullField()

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction()
    }

    companion object {

        fun newInstance() =
            CreateTaskFragment()
    }
}
