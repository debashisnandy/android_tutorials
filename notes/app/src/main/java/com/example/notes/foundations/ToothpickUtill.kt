package com.example.notes.foundations

import com.example.notes.notes.INoteModel
import com.example.notes.notes.NoteLocalModel
import com.example.notes.tasks.ITaskModel
import com.example.notes.tasks.TaskLocalModel
import toothpick.Toothpick
import toothpick.config.Module

object ApplicationScope{
    val scope = Toothpick.openScope(this).apply {
        installModules(ApplicationModule)
    }
}

object ApplicationModule: Module(){
    init {
        bind(INoteModel::class.java).toInstance(NoteLocalModel())
        bind(ITaskModel::class.java).toInstance(TaskLocalModel())
    }
}