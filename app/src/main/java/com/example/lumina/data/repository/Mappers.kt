package com.example.lumina.data.repository

import com.example.lumina.data.model.JournalEntryEntity
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.model.Mood

fun JournalEntryEntity.toDomain(): JournalEntry {
    val domainMood = runCatching {
        Mood.valueOf(mood.trim().uppercase())
    }.getOrDefault(Mood.CALM)
    return JournalEntry(id, title, content, date, domainMood)
}

fun JournalEntry.toEntity(
    lastUpdated: Long = System.currentTimeMillis(),
    isSynced: Boolean = false,
    isDeleted: Boolean = false
): JournalEntryEntity =
    JournalEntryEntity(id, title, content, date, mood.name, lastUpdated, isSynced, isDeleted)
