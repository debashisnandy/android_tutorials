package com.example.notes.tasks

interface TaskViewContract {
    fun onTodoUpdate(taskIndex: Int, todoIndex: Int, isCompleted: Boolean)
    fun onTaskDeleted(taskIndex: Int)
}