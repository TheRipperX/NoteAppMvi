package com.example.noteappmvi.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappmvi.data.model.NoteEntity
import com.example.noteappmvi.data.repository.DetailRepository
import com.example.noteappmvi.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository): ViewModel() {

    val detailIntent = Channel<DetailIntent>()

    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)
    val state: StateFlow<DetailState> get() = _state

    init {
        handelIntent()
    }

    private fun handelIntent() = viewModelScope.launch {

        detailIntent.consumeAsFlow().collect {
            when(it) {
                is DetailIntent.SpinnerList -> { setSpinnerData()}
                is DetailIntent.SaveNote -> { saveNotes(it.noteEntity)}
            }
        }
    }

    private fun setSpinnerData() {
        val categoryList = mutableListOf(HEALTHY, HOME, WORK, LEARNING)
        val priorityList = mutableListOf(HIGH, NORMAL, LOW)
        _state.value = DetailState.SpinnerList(categoryList, priorityList)
    }

    private fun saveNotes(noteEntity: NoteEntity) = viewModelScope.launch {
        _state.value = try {
            DetailState.SaveNote(detailRepository.insertNote(noteEntity))
        }
        catch (e: Exception) {
            DetailState.Error(e.message.toString())
        }
    }



}