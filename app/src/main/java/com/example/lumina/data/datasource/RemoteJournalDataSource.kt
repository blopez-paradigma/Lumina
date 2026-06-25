package com.example.lumina.data.datasource

import com.example.lumina.data.remote.api.LuminaApiService
import com.example.lumina.data.remote.dto.JournalEntryDto
import javax.inject.Inject

class RemoteJournalDataSource @Inject constructor(
    private val api: LuminaApiService
) {
    suspend fun getEntries(): List<JournalEntryDto> = api.getEntries()

    suspend fun createEntry(entry: JournalEntryDto): JournalEntryDto = api.createEntry(entry)

    suspend fun updateEntry(id: Long, entry: JournalEntryDto): JournalEntryDto = api.updateEntry(id, entry)

    suspend fun deleteEntry(id: Long) = api.deleteEntry(id)
}
