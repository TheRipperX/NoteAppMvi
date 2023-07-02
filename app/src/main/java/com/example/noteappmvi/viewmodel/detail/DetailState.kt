package com.example.noteappmvi.viewmodel.detail

sealed class DetailState {
    object Idle: DetailState()
    data class SpinnerList(val category: MutableList<String>, val priority: MutableList<String>): DetailState()
    data class Error(val error: String): DetailState()
    data class SaveNote(val unit: Unit): DetailState()
}
