package com.example.lumina.data.repository

import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.remote.api.LuminaApiService
import com.example.lumina.data.remote.dto.toDto
import com.example.lumina.data.remote.dto.toJournalEntryEntity
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncedJournalRepository @Inject constructor(
    private val dao: JournalDao,
    private val api: LuminaApiService
) : JournalRepository {

    override fun getAllEntriesStream(): Flow<List<JournalEntry>> =
        dao.getAllEntries().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getEntryStream(id: Long): JournalEntry? =
        dao.getEntryById(id)?.toDomain()

    override suspend fun insertEntry(entry: JournalEntry) {
        dao.insertEntry(entry.toEntity())
        runCatching { api.createEntry(entry.toDto()) }
    }

    override suspend fun updateEntry(entry: JournalEntry) {
        dao.updateEntry(entry.toEntity())
        runCatching { api.updateEntry(entry.id, entry.toDto()) }
    }

    override suspend fun deleteEntry(entry: JournalEntry) {
        dao.deleteEntry(entry.toEntity())
        runCatching { api.deleteEntry(entry.id) }
    }

    override suspend fun syncFromRemote(): Result<Unit> = runCatching {
        val remoteEntities = api.getEntries().map { it.toJournalEntryEntity() }
        remoteEntities.forEach { dao.insertEntry(it) }
    }
}
