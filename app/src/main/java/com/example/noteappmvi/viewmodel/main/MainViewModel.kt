package com.example.noteappmvi.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}