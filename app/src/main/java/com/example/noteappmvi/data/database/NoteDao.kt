package com.example.noteappmvi.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteappmvi.data.model.NoteEntity
import com.example.noteappmvi.utils.NOTE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM $NOTE_TABLE")
    fun getAllNote(): Flow<MutableList<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE id = :id ")
    fun findNoteId(id: Int): Flow<NoteEntity>

    @Query("SELECT * FROM $NOTE_TABLE WHERE title LIKE '%' || :str || '%'")
    fun searchNote(str: String): Flow<MutableList<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE priority = :str ")
    fun filterNote(str: String): Flow<MutableList<NoteEntity>>

}