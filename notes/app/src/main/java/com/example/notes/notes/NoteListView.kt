package com.example.notes.notes

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.models.Note
import kotlinx.android.synthetic.main.fragment_note_list.view.*

class NoteListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr
) {

    private lateinit var touchActionDelegate: NoteListFragment.TouchActionDelegate
    private lateinit var daActionDelegate: NoteListViewContract
    private lateinit var adapter: NoteAdapter

    internal fun iniView(taDelegate: NoteListFragment.TouchActionDelegate, daDelegate: NoteListViewContract) {
        setDelegate(taDelegate, daDelegate)
        setUpView()
    }

    private fun setDelegate(taDelegate: NoteListFragment.TouchActionDelegate, daDelegate: NoteListViewContract) {
        touchActionDelegate = taDelegate
        daActionDelegate = daDelegate
    }


    private fun setUpView() {
        recycler2View.layoutManager = LinearLayoutManager(context)
        adapter = NoteAdapter(touchActionDelegate = touchActionDelegate,daDelegate = daActionDelegate)
        recycler2View.adapter = adapter
    }

    internal fun updateNotes(notes: List<Note>) {
        adapter.uiUpdate(notes)
    }


}