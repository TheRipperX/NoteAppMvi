package com.example.noteappmvi.data.repository

import com.example.noteappmvi.data.database.NoteDao
import com.example.noteappmvi.data.model.NoteEntity
import javax.inject.Inject

class HomeRepository @Inject constructor(private val dao: NoteDao) {

    fun getAllNotes() = dao.getAllNote()
    fun searchNote(str: String) = dao.searchNote(str = str)
    fun filterNote(str: String) = dao.filterNote(str = str)
    suspend fun deleteNote(noteEntity: NoteEntity) = dao.deleteNote(noteEntity = noteEntity)
}