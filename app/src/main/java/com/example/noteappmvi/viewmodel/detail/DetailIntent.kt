package com.example.noteappmvi.viewmodel.detail

import com.example.noteappmvi.data.model.NoteEntity

sealed class DetailIntent {
    object SpinnerList: DetailIntent()
    data class SaveNote(val noteEntity: NoteEntity): DetailIntent()
}