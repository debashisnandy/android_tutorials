package com.example.notes.notes

import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.foundations.BaseRecyclerAdapter
import com.example.notes.models.Note
import com.example.notes.navigation.NavigationActivity
import com.example.notes.views.NoteView
import kotlinx.android.synthetic.main.view_add_button.view.*

class NoteAdapter(
    listNote: MutableList<Note> = mutableListOf(),
    val touchActionDelegate: NoteListFragment.TouchActionDelegate,
    val daDelegate: NoteListViewContract
) : BaseRecyclerAdapter<Note>(listNote) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_INFO) {
            NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))
        } else {
            AddButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_add_button, parent, false))
        }

    inner class NoteViewHolder(view: View) : BaseViewHolder<Note>(view) {

        override fun onBind(data: Note, listIndex: Int) {
            (view as NoteView).initView(data){
                daDelegate.onDeleteNote(masterList[listIndex])
            }
        }
    }


    inner class AddButtonViewHolder(view: View) : BaseRecyclerAdapter.AddButtonViewHolder(view) {
        override fun onBind(data: Unit, listIndex: Int) {
            view.buttonText.text = view.context.getString(R.string.add_button_note)
            view.setOnClickListener {
                touchActionDelegate.onAddButtonClicked(NavigationActivity.FRAGMENT_TYPE_NOTE)
            }
        }
    }
}