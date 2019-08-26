package com.example.notes.notes

import com.example.notes.models.Note

typealias SuccessCallBack = (Boolean) -> Unit

interface INoteModel {

    suspend fun addNote(note:Note, callBack: SuccessCallBack)
    suspend fun updateNote(note:Note, callBack: SuccessCallBack)
    suspend fun deleteNote(note:Note, callBack: SuccessCallBack)
    suspend fun retriveNote(callBack: (List<Note>?) -> Unit)

}