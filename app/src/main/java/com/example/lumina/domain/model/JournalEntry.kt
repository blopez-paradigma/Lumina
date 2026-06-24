package com.example.lumina.domain.model

data class JournalEntry(
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: Long,
    val mood: Mood
)
