package com.example.lumina.ui.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lumina.data.model.JournalEntry
import com.example.lumina.data.repository.JournalRepository
import com.example.lumina.data.model.Mood
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel to validate and insert items in the Room database.
 */
@HiltViewModel
class EntryViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var entryUiState by mutableStateOf(EntryUiState())
        private set

    /**
     * Updates the [entryUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(entryDetails: EntryDetails) {
        entryUiState =
            EntryUiState(entryDetails = entryDetails, isEntryValid = validateInput(entryDetails))
    }

    /**
     * Inserts a [JournalEntry] in the Room database
     */
    suspend fun saveEntry() {
        if (validateInput(entryUiState.entryDetails)) {
            journalRepository.insertEntry(entryUiState.entryDetails.toJournalEntry())
        }
    }

    private fun validateInput(uiState: EntryDetails = entryUiState.entryDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && content.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
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

/**
 * Extension function to convert [EntryDetails] to [JournalEntry]
 */
fun EntryDetails.toJournalEntry(): JournalEntry = JournalEntry(
    id = id,
    title = title,
    content = content,
    date = date,
    mood = mood
)

/**
 * Extension function to convert [JournalEntry] to [EntryDetails]
 */
fun JournalEntry.toEntryDetails(): EntryDetails = EntryDetails(
    id = id,
    title = title,
    content = content,
    date = date,
    mood = mood
)
