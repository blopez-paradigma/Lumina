package com.example.lumina.data.repository

import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.model.JournalEntry

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides methods to manage and access [JournalEntry] data from the underlying data
 */
interface JournalRepository {
    fun getAllEntriesStream(): Flow<List<JournalEntry>>
    suspend fun getEntryStream(id: Long): JournalEntry?
    suspend fun insertEntry(entry: JournalEntry)
    suspend fun deleteEntry(entry: JournalEntry)
    suspend fun updateEntry(entry: JournalEntry)
}

class OfflineJournalRepository(private val journalDao: JournalDao) : JournalRepository {
    override fun getAllEntriesStream(): Flow<List<JournalEntry>> = journalDao.getAllEntries()
    override suspend fun getEntryStream(id: Long): JournalEntry? = journalDao.getEntryById(id)
    override suspend fun insertEntry(entry: JournalEntry) = journalDao.insertEntry(entry)
    override suspend fun deleteEntry(entry: JournalEntry) = journalDao.deleteEntry(entry)
    override suspend fun updateEntry(entry: JournalEntry) = journalDao.updateEntry(entry)
}
