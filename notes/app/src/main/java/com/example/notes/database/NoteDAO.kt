package com.example.notes.database

import androidx.room.*
import com.example.notes.models.Note


@Dao
interface NoteDAO {

    @Insert
    fun addNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)


    @Query("SELECT * FROM notes")
    fun retriveNotes():MutableList<Note>
}