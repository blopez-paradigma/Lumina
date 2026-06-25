package com.example.lumina.data.repository

import com.example.lumina.data.datasource.LocalJournalDataSource
import com.example.lumina.data.datasource.RemoteJournalDataSource
import com.example.lumina.data.remote.dto.toDto
import com.example.lumina.data.remote.dto.toJournalEntryEntity
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncedJournalRepository @Inject constructor(
    private val localDataSource: LocalJournalDataSource,
    private val remoteDataSource: RemoteJournalDataSource
) : JournalRepository {

    override fun getAllEntriesStream(): Flow<List<JournalEntry>> =
        localDataSource.getAllEntriesStream().map { entities -> 
            entities.map { it.toDomain() } 
        }

    override suspend fun getEntryStream(id: Long): JournalEntry? =
        localDataSource.getEntryById(id)?.toDomain()

    override suspend fun insertEntry(entry: JournalEntry) {
        val entity = entry.toEntity(isSynced = false)
        localDataSource.insertEntry(entity)
        syncUnsyncedEntry(entry)
    }

    override suspend fun updateEntry(entry: JournalEntry) {
        val entity = entry.toEntity(isSynced = false)
        localDataSource.updateEntry(entity)
        syncUnsyncedEntry(entry)
    }

    override suspend fun deleteEntry(entry: JournalEntry) {
        // Soft delete locally
        val entity = entry.toEntity(isSynced = false, isDeleted = true)
        localDataSource.updateEntry(entity)
        
        runCatching {
            remoteDataSource.deleteEntry(entry.id)
        }.onSuccess {
            // Hard delete locally after successful remote delete
            localDataSource.deleteEntry(entity)
        }
    }

    override suspend fun syncFromRemote(): Result<Unit> = runCatching {
        // 1. Push pending deletions
        localDataSource.getPendingDeletions().forEach { entity ->
            runCatching {
                remoteDataSource.deleteEntry(entity.id)
                localDataSource.deleteEntry(entity)
            }
        }

        // 2. Push unsynced updates/creations
        localDataSource.getUnsyncedEntries()
            .filter { !it.isDeleted }
            .forEach { entity ->
                runCatching {
                    remoteDataSource.updateEntry(entity.id, entity.toDomain().toDto())
                    localDataSource.updateEntry(entity.copy(isSynced = true))
                }
            }

        // 3. Fetch remote changes
        val remoteEntries = remoteDataSource.getEntries()
        
        // 4. Conflict resolution: only update if not dirty locally
        val localUnsyncedIds = localDataSource.getUnsyncedEntries().map { it.id }.toSet()
        val entitiesToUpdate = remoteEntries
            .map { it.toJournalEntryEntity() }
            .filter { it.id !in localUnsyncedIds }
            
        localDataSource.updateEntries(entitiesToUpdate)
    }

    private suspend fun syncUnsyncedEntry(entry: JournalEntry) {
        runCatching {
            remoteDataSource.updateEntry(entry.id, entry.toDto())
            localDataSource.updateEntry(entry.toEntity(isSynced = true))
        }
    }
}
