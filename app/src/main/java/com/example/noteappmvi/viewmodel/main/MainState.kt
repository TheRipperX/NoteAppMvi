package com.example.noteappmvi.viewmodel.main

import com.example.noteappmvi.data.model.NoteEntity

sealed class MainState {
    object Empty: MainState()
    data class AllNotes(val list: MutableList<NoteEntity>): MainState()
    data class DeleteNote(val unit: Unit): MainState()
}
