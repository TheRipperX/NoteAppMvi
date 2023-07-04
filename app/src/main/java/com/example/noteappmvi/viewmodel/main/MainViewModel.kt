package com.example.noteappmvi.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappmvi.data.model.NoteEntity
import com.example.noteappmvi.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Empty)
    val state: StateFlow<MainState> get() = _state

    /*init {
        handelIntent()
    }*/

    fun handelIntent(intent: MainIntent) {
        when(intent) {
            is MainIntent.AllNotes -> { getAllNote() }
            is MainIntent.DeleteNote -> { deleteNote(intent.noteEntity) }
            is MainIntent.FilterNote -> { filterNote(intent.filter) }
            is MainIntent.SearchNote -> { searchNote(intent.search) }
        }
    }

    private fun getAllNote() = viewModelScope.launch {
        val data = homeRepository.getAllNotes()

        data.collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.AllNotes(it)
            }else {
                MainState.Empty
            }
        }

    }

    private fun deleteNote(noteEntity: NoteEntity) = viewModelScope.launch {
        _state.value = MainState.DeleteNote(homeRepository.deleteNote(noteEntity = noteEntity))
    }

    private fun filterNote(str: String) = viewModelScope.launch {

        homeRepository.filterNote(str = str).collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.AllNotes(it)
            }else {
                MainState.Empty
            }
        }
    }

    private fun searchNote(str: String) = viewModelScope.launch {

        homeRepository.searchNote(str).collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.AllNotes(it)
            }else {
                MainState.Empty
            }
        }
    }


}