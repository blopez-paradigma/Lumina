package com.example.lumina.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumina.data.model.JournalEntry
import com.example.lumina.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel to retrieve all items in the Room database.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    journalRepository: JournalRepository
) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [JournalRepository] and mapped to [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        journalRepository.getAllEntriesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<JournalEntry> = listOf())
