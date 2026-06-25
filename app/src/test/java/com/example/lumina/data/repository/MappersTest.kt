package com.example.lumina.data.repository

import com.example.lumina.data.model.JournalEntryEntity
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.model.Mood
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun `toDomain maps valid uppercase mood string correctly`() {
        val entity = JournalEntryEntity(
            id = 1,
            title = "Title",
            content = "Content",
            date = 12345L,
            mood = "HAPPY"
        )
        val domain = entity.toDomain()
        assertEquals(Mood.HAPPY, domain.mood)
    }

    @Test
    fun `toDomain maps mixed case and trimmed mood string correctly`() {
        val entity = JournalEntryEntity(
            id = 1,
            title = "Title",
            content = "Content",
            date = 12345L,
            mood = "  pEaCeFuL  "
        )
        val domain = entity.toDomain()
        assertEquals(Mood.PEACEFUL, domain.mood)
    }

    @Test
    fun `toDomain maps invalid mood string to fallback CALM`() {
        val entity = JournalEntryEntity(
            id = 1,
            title = "Title",
            content = "Content",
            date = 12345L,
            mood = "UNKNOWN_MOOD"
        )
        val domain = entity.toDomain()
        assertEquals(Mood.CALM, domain.mood)
    }

    @Test
    fun `toDomain maps empty mood string to fallback CALM`() {
        val entity = JournalEntryEntity(
            id = 1,
            title = "Title",
            content = "Content",
            date = 12345L,
            mood = ""
        )
        val domain = entity.toDomain()
        assertEquals(Mood.CALM, domain.mood)
    }

    @Test
    fun `toEntity maps domain entry correctly with mood name as string`() {
        val domain = JournalEntry(
            id = 2,
            title = "Another Title",
            content = "Another Content",
            date = 54321L,
            mood = Mood.ANGRY
        )
        val entity = domain.toEntity()
        assertEquals(2L, entity.id)
        assertEquals("Another Title", entity.title)
        assertEquals("Another Content", entity.content)
        assertEquals(54321L, entity.date)
        assertEquals("ANGRY", entity.mood)
    }
}
