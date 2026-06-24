package com.example.lumina.ui.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.model.Mood
import com.example.lumina.domain.usecase.SaveEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val saveEntryUseCase: SaveEntryUseCase
) : ViewModel() {

    var entryUiState by mutableStateOf(EntryUiState())
        private set

    fun updateUiState(entryDetails: EntryDetails) {
        entryUiState =
            EntryUiState(entryDetails = entryDetails, isEntryValid = validateInput(entryDetails))
    }

    suspend fun saveEntry() {
        if (validateInput(entryUiState.entryDetails)) {
            saveEntryUseCase(entryUiState.entryDetails.toJournalEntry())
        }
    }

    private fun validateInput(uiState: EntryDetails = entryUiState.entryDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && content.isNotBlank()
        }
    }
}

data class EntryUiState(
    val entryDetails: EntryDetails = EntryDetails(),
    val isEntryValid: Boolean = false
)

data class EntryDetails(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val date: Long = System.currentTimeMillis(),
    val mood: Mood = Mood.HAPPY
)

fun EntryDetails.toJournalEntry(): JournalEntry = JournalEntry(
    id = id,
    title = title,
    content = content,
    date = date,
    mood = mood
)

fun JournalEntry.toEntryDetails(): EntryDetails = EntryDetails(
    id = id,
    title = title,
    content = content,
    date = date,
    mood = mood
)
