package com.example.notes.notes

import android.util.Log
import com.example.notes.application.NoteApplication
import com.example.notes.database.RoomDatabaseClient
import com.example.notes.models.Note
import kotlinx.coroutines.*
import javax.inject.Inject

const val TIMEOUT_DURATION_MILLIS = 3000L

class NoteLocalModel @Inject constructor() : INoteModel{

    private val databaseClient = RoomDatabaseClient.getInstance(NoteApplication.instance.applicationContext)


    private suspend fun performOperationWithTimeout(function: () -> Unit,callBack: SuccessCallBack){

            val job = GlobalScope.async {
                try {
                    withTimeout(TIMEOUT_DURATION_MILLIS){
                        function.invoke()
                    }

                }catch (e:Exception){
                    callBack.invoke(false)
                }
            }
            job.await()
            callBack.invoke(true)

    }

    override suspend fun addNote(note: Note, callBack: SuccessCallBack) {
        performOperationWithTimeout({databaseClient.noteDao().addNote(note)},callBack)
    }

    override suspend fun updateNote(note: Note, callBack: SuccessCallBack) {
        performOperationWithTimeout({databaseClient.noteDao().updateNote(note)},callBack)

    }

    override suspend fun deleteNote(note: Note, callBack: SuccessCallBack) {
        performOperationWithTimeout({databaseClient.noteDao().deleteNote(note)},callBack)
    }



    override suspend fun retriveNote(callBack: (List<Note>?) -> Unit){
        GlobalScope.launch {

            val job = async {
                withTimeoutOrNull(TIMEOUT_DURATION_MILLIS){
                    databaseClient.noteDao().retriveNotes()
                }
            }
            callBack.invoke(job.await())
        }
    }
}