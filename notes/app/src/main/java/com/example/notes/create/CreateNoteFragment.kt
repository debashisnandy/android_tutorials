package com.example.notes.create


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.notes.R
import com.example.notes.foundations.ApplicationScope
import com.example.notes.foundations.NullFieldChecker
import com.example.notes.models.Note
import com.example.notes.notes.INoteModel
import com.example.notes.tasks.ITaskModel
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.Toothpick
import javax.inject.Inject


class CreateNoteFragment : Fragment(), NullFieldChecker {


    @Inject
    lateinit var model:INoteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, ApplicationScope.scope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    fun saveNote(callback: (Boolean)-> Unit){
        GlobalScope.launch {
            createNote()?.let {note->
                model.addNote(note){
                    callback.invoke(it)
                }
            }?: callback.invoke(false)
        }

    }

    fun createNote(): Note? =
        if (!hasNullField()){
             Note( description =  noteText.editableText.toString())
        }else{
            null
        }


    override fun hasNullField(): Boolean = noteText.editableText.isNullOrEmpty()

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment()
    }
}
