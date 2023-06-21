package com.example.noteappmvi.data.repository

import com.example.noteappmvi.data.database.NoteDao
import com.example.noteappmvi.data.model.NoteEntity
import javax.inject.Inject

class DetailRepository @Inject constructor(private val dao: NoteDao) {

    suspend fun insertNote(noteEntity: NoteEntity) = dao.insertNote(noteEntity = noteEntity)
    suspend fun updateNote(noteEntity: NoteEntity) = dao.updateNote(noteEntity = noteEntity)
    fun findNote(id: Int) = dao.findNoteId(id = id)

}