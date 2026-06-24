package com.example.lumina.data.remote.dto

import com.example.lumina.data.model.JournalEntry
import com.example.lumina.data.model.Mood
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JournalEntryDto(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "userId") val userId: Int = 1,
    @Json(name = "title") val title: String,
    @Json(name = "body") val body: String
)

fun JournalEntryDto.toJournalEntry(): JournalEntry = JournalEntry(
    id = id,
    title = title,
    content = body,
    date = System.currentTimeMillis(),
    mood = Mood.CALM
)

fun JournalEntry.toDto(): JournalEntryDto = JournalEntryDto(
    id = id,
    userId = 1,
    title = title,
    body = content
)
