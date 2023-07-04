package com.example.noteappmvi.viewmodel.main

import com.example.noteappmvi.data.model.NoteEntity

sealed class MainIntent {
    object AllNotes: MainIntent()
    data class DeleteNote(val noteEntity: NoteEntity): MainIntent()
    data class SearchNote(val search: String): MainIntent()
    data class FilterNote(val filter: String): MainIntent()
}
