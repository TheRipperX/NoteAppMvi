package com.example.noteappmvi.viewmodel.detail

import com.example.noteappmvi.data.model.NoteEntity

sealed class DetailState {
    object Idle: DetailState()
    data class SpinnerList(val category: MutableList<String>, val priority: MutableList<String>): DetailState()
    data class Error(val error: String): DetailState()
    data class SaveNote(val unit: Unit): DetailState()
    data class EditNote(val unit: Unit): DetailState()
    data class DetailNote(val noteEntity: NoteEntity): DetailState()
}
