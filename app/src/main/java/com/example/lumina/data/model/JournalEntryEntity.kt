package com.example.lumina.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lumina.domain.model.Mood

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: Long,
    val mood: Mood
)
