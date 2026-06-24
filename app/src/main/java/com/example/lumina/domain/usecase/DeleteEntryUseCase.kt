package com.example.lumina.domain.usecase

import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.repository.JournalRepository
import javax.inject.Inject

class DeleteEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(entry: JournalEntry) = repository.deleteEntry(entry)
}
