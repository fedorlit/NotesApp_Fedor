package com.example.notesapp_fedor.presentation

import com.example.notesapp_fedor.data.Note

sealed interface NotesEvent {
    object SortNotes: NotesEvent

    data class DeleteNote(val note: Note):NotesEvent

    data class SaveNote(val title:String, val description: String):NotesEvent
}