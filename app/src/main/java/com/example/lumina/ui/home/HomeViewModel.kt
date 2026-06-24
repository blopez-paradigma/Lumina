package com.example.lumina.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.repository.JournalRepository
import com.example.lumina.domain.usecase.GetAllEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllEntries: GetAllEntriesUseCase,
    private val journalRepository: JournalRepository
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        getAllEntries().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    init {
        viewModelScope.launch {
            journalRepository.syncFromRemote()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val itemList: List<JournalEntry> = listOf())
