package com.example.lumina.data.repository

import com.example.lumina.data.local.JournalDao
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineJournalRepository(private val journalDao: JournalDao) : JournalRepository {

    override fun getAllEntriesStream(): Flow<List<JournalEntry>> =
        journalDao.getAllEntries().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getEntryStream(id: Long): JournalEntry? =
        journalDao.getEntryById(id)?.toDomain()

    override suspend fun insertEntry(entry: JournalEntry) = journalDao.insertEntry(entry.toEntity())

    override suspend fun deleteEntry(entry: JournalEntry) = journalDao.deleteEntry(entry.toEntity())

    override suspend fun updateEntry(entry: JournalEntry) = journalDao.updateEntry(entry.toEntity())

    override suspend fun syncFromRemote(): Result<Unit> = Result.success(Unit)
}
