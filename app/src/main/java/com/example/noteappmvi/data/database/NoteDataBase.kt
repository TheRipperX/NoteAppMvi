package com.example.noteappmvi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteappmvi.data.model.NoteEntity
import com.example.noteappmvi.utils.DATABASE_VERSION

@Database(entities = [NoteEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class NoteDataBase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}