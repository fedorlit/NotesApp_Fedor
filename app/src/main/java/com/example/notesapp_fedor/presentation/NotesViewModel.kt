package com.example.notesapp_fedor.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp_fedor.data.Note
import com.example.notesapp_fedor.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val dao: NoteDao
):ViewModel(){

    private val isSortedbyDateAdded = MutableStateFlow(true)

    private val notes= isSortedbyDateAdded.flatMapLatest { sort ->
        if (sort){
            dao.getNotesOrderedByDateAdded()
        }else{
            dao.getNotesOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(NoteState())
    val state = combine(_state, isSortedbyDateAdded, notes){ state, isSorted, notes ->
        state.copy(
            notes = notes,
            isSortedByDateAdded = isSorted
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),NoteState())

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NotesEvent.SaveNote -> {
                val title = state.value.title.value
                val description = state.value.description.value
                
                if(title.isBlank() && description.isBlank()) {
                    return
                }

                val note = Note(
                        title = title,
                        description = description,
                        dateAdded = System.currentTimeMillis()
                    )
                    viewModelScope.launch {
                        dao.upsertNote(note)
                    }
                    _state.update {
                        it.copy(
                            title = mutableStateOf(""),
                            description = mutableStateOf("")
                        )
                    }
            }
            NotesEvent.SortNotes -> {
                isSortedbyDateAdded.value = !isSortedbyDateAdded.value
            }
        }
    }
}