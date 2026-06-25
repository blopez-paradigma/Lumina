package com.example.lumina.data.datasource

import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.model.JournalEntryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalJournalDataSource @Inject constructor(
    private val journalDao: JournalDao
) {
    fun getAllEntriesStream(): Flow<List<JournalEntryEntity>> = journalDao.getAllEntries()

    suspend fun getEntryById(id: Long): JournalEntryEntity? = journalDao.getEntryById(id)

    suspend fun insertEntry(entry: JournalEntryEntity) = journalDao.insertEntry(entry)

    suspend fun getUnsyncedEntries(): List<JournalEntryEntity> = journalDao.getUnsyncedEntries()

    suspend fun getPendingDeletions(): List<JournalEntryEntity> = journalDao.getPendingDeletions()



    suspend fun updateEntry(entry: JournalEntryEntity) = journalDao.updateEntry(entry)

    suspend fun deleteEntry(entry: JournalEntryEntity) = journalDao.deleteEntry(entry)
    
    @androidx.room.Transaction
    suspend fun updateEntries(entries: List<JournalEntryEntity>) {
        entries.forEach { journalDao.insertEntry(it) }
    }
}
