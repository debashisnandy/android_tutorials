package com.example.notes.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.notes.models.Todo
import kotlinx.android.synthetic.main.view_todo.view.*

class TodoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr
) {

    fun initView(todo: Todo, callBack: ((Boolean) -> Unit)?) {

        descriptionView!!.text = todo.description
        completeCheckBox!!.isChecked = todo.isComplete
        if (todo.isComplete) {
            descriptionView.setStrikeThrough()
        }
        setupCheckStateListener(todo, callBack)
    }

    private fun setupCheckStateListener(todo: Todo, callBack: ((Boolean) -> Unit)? = null) {
        completeCheckBox!!.setOnCheckedChangeListener { _, ischecked ->
            todo.isComplete = ischecked

            callBack?.invoke(ischecked)

            if (ischecked) {
                descriptionView.setStrikeThrough()
            } else {
                descriptionView.removeStrikeThrough()
            }
        }
    }

}
