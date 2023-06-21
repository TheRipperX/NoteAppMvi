package com.example.noteappmvi.utils

import android.content.Context
import androidx.room.Room
import com.example.noteappmvi.data.database.NoteDataBase
import com.example.noteappmvi.data.model.NoteEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun proDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context = context, klass = NoteDataBase::class.java, name = DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun proDao(dataBase: NoteDataBase) = dataBase.noteDao()

    @Provides
    @Singleton
    fun proEntity() = NoteEntity()

}