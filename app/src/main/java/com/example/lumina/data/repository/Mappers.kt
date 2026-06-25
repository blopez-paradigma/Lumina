package com.example.lumina.data.repository

import com.example.lumina.data.model.JournalEntryEntity
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.model.Mood

fun JournalEntryEntity.toDomain(): JournalEntry {
    val domainMood = try {
        Mood.valueOf(mood.trim().uppercase())
    } catch (e: IllegalArgumentException) {
        Mood.CALM
    }
    return JournalEntry(id, title, content, date, domainMood)
}

fun JournalEntry.toEntity(): JournalEntryEntity =
    JournalEntryEntity(id, title, content, date, mood.name)
