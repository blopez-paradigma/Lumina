package com.example.lumina.data.repository

import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.model.JournalEntry
import com.example.lumina.data.remote.api.LuminaApiService
import com.example.lumina.data.remote.dto.toDto
import com.example.lumina.data.remote.dto.toJournalEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncedJournalRepository @Inject constructor(
    private val dao: JournalDao,
    private val api: LuminaApiService
) : JournalRepository {

    override fun getAllEntriesStream(): Flow<List<JournalEntry>> = dao.getAllEntries()

    override suspend fun getEntryStream(id: Long): JournalEntry? = dao.getEntryById(id)

    override suspend fun insertEntry(entry: JournalEntry) {
        dao.insertEntry(entry)
        runCatching { api.createEntry(entry.toDto()) }
    }

    override suspend fun updateEntry(entry: JournalEntry) {
        dao.updateEntry(entry)
        runCatching { api.updateEntry(entry.id, entry.toDto()) }
    }

    override suspend fun deleteEntry(entry: JournalEntry) {
        dao.deleteEntry(entry)
        runCatching { api.deleteEntry(entry.id) }
    }

    override suspend fun syncFromRemote(): Result<Unit> = runCatching {
        val remoteEntries = api.getEntries().map { it.toJournalEntry() }
        remoteEntries.forEach { dao.insertEntry(it) }
    }
}
