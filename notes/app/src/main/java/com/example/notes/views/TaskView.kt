package com.example.notes.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.notes.R
import com.example.notes.models.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr
) {

    private lateinit var task: Task

    fun initView(task: Task, todoCheckedCallback: ((Int, Boolean) -> (Unit))? = null, deleteTaskCallbacks:() -> Unit) {

        resetChildView()

        this.task = task

        initTaskLine { deleteTaskCallbacks.invoke() }

        addChildView(todoCheckedCallback)
    }

    private fun resetChildView(){
        todoContainer.removeAllViewsInLayout()
    }

    private fun initTaskLine(deleteCallbacks: () -> Unit){
        taskTitle.text = task.title
        imgDeleteTask.setOnClickListener {
            deleteCallbacks.invoke()
        }
        if (isTaskComplete()) {
            this@TaskView.taskTitle.setStrikeThrough()
        } else {
            this@TaskView.taskTitle.removeStrikeThrough()
        }
    }

    private fun addChildView(todoCheckedCallback: ((Int, Boolean) -> (Unit))? = null){
        task.todos.forEachIndexed { todoIndex, todo ->
            val todoView =
                (LayoutInflater.from(context).inflate(R.layout.view_todo, todoContainer, false) as TodoView).apply {
                    initView(todo) {
                        todoCheckedCallback?.invoke(todoIndex, it)
                    }
                }
            todoContainer.addView(todoView)
        }
    }

    private fun isTaskComplete(): Boolean = task.todos.filter { !it.isComplete }.isEmpty()

}