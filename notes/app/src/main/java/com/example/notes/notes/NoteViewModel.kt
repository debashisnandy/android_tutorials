package com.example.notes.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notes.foundations.ApplicationScope
import com.example.notes.models.Note
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

class NoteViewModel : ViewModel(), NoteListViewContract {

    @Inject
    lateinit var model: INoteModel

    private var _noteListLiveData: MutableLiveData<MutableList<Note>> = MutableLiveData()
    internal var noteListLivedata: LiveData<MutableList<Note>> = _noteListLiveData

    init {
        Toothpick.inject(this,ApplicationScope.scope)
        loadData()
    }

    fun loadData(){
        GlobalScope.launch {
            model.retriveNote {
                it?.let {
                    _noteListLiveData.postValue(it.toMutableList())
                }
            }
        }


    }

    override fun onDeleteNote(note: Note) {
        GlobalScope.launch {
            model.deleteNote(note){
                if (it){
                    loadData()
                }
            }
        }
    }
}