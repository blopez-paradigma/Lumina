package com.example.lumina.domain.usecase

import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEntriesUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    operator fun invoke(): Flow<List<JournalEntry>> = repository.getAllEntriesStream()
}
