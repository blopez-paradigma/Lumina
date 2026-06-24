package com.example.lumina.data.repository

import com.example.lumina.data.model.JournalEntryEntity
import com.example.lumina.domain.model.JournalEntry

fun JournalEntryEntity.toDomain(): JournalEntry =
    JournalEntry(id, title, content, date, mood)

fun JournalEntry.toEntity(): JournalEntryEntity =
    JournalEntryEntity(id, title, content, date, mood)
