package com.example.lumina.domain.repository

import com.example.lumina.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getAllEntriesStream(): Flow<List<JournalEntry>>
    suspend fun getEntryStream(id: Long): JournalEntry?
    suspend fun insertEntry(entry: JournalEntry)
    suspend fun deleteEntry(entry: JournalEntry)
    suspend fun updateEntry(entry: JournalEntry)
    suspend fun syncFromRemote(): Result<Unit>
}
