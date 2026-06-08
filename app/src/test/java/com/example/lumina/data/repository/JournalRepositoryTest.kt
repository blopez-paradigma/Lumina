package com.example.lumina.data.repository

import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.model.JournalEntry
import com.example.lumina.data.model.Mood
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class JournalRepositoryTest {

    private lateinit var repository: OfflineJournalRepository
    private val dao: JournalDao = mockk()

    @Before
    fun setUp() {
        repository = OfflineJournalRepository(dao)
    }

    @Test
    fun `insertEntry calls dao insertEntry`() = runTest {
        val entry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        coEvery { dao.insertEntry(entry) } returns Unit
        
        repository.insertEntry(entry)
        
        coVerify(exactly = 1) { dao.insertEntry(entry) }
    }

    @Test
    fun `deleteEntry calls dao deleteEntry`() = runTest {
        val entry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        coEvery { dao.deleteEntry(entry) } returns Unit
        
        repository.deleteEntry(entry)
        
        coVerify(exactly = 1) { dao.deleteEntry(entry) }
    }

    @Test
    fun `updateEntry calls dao updateEntry`() = runTest {
        val entry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        coEvery { dao.updateEntry(entry) } returns Unit
        
        repository.updateEntry(entry)
        
        coVerify(exactly = 1) { dao.updateEntry(entry) }
    }

    @Test
    fun `getEntryStream returns correct entry`() = runTest {
        val entry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        coEvery { dao.getEntryById(1) } returns entry
        
        val result = repository.getEntryStream(1)
        
        assertEquals(entry, result)
        coVerify(exactly = 1) { dao.getEntryById(1) }
    }

    @Test
    fun `getAllEntriesStream returns all entries`() = runTest {
        val entries = listOf(
            JournalEntry(1, "Title 1", "Content 1", 12345L, Mood.HAPPY),
            JournalEntry(2, "Title 2", "Content 2", 12346L, Mood.CALM)
        )
        every { dao.getAllEntries() } returns flowOf(entries)
        
        val result = repository.getAllEntriesStream().first()
        
        assertEquals(entries, result)
        verify(exactly = 1) { dao.getAllEntries() }
    }
}
