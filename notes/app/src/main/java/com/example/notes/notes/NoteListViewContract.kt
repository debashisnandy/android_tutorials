package com.example.notes.notes

import com.example.notes.models.Note

interface NoteListViewContract {
    fun onDeleteNote(note: Note)
}