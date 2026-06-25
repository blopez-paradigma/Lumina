package com.example.lumina.data.repository

import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.model.JournalEntryEntity
import com.example.lumina.domain.model.JournalEntry
import com.example.lumina.domain.model.Mood
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
        val domainEntry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        val entityEntry = JournalEntryEntity(1, "Title", "Content", 12345L, Mood.HAPPY.name)
        coEvery { dao.insertEntry(entityEntry) } returns Unit

        repository.insertEntry(domainEntry)

        coVerify(exactly = 1) { dao.insertEntry(entityEntry) }
    }

    @Test
    fun `deleteEntry calls dao deleteEntry`() = runTest {
        val domainEntry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        val entityEntry = JournalEntryEntity(1, "Title", "Content", 12345L, Mood.HAPPY.name)
        coEvery { dao.deleteEntry(entityEntry) } returns Unit

        repository.deleteEntry(domainEntry)

        coVerify(exactly = 1) { dao.deleteEntry(entityEntry) }
    }

    @Test
    fun `updateEntry calls dao updateEntry`() = runTest {
        val domainEntry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        val entityEntry = JournalEntryEntity(1, "Title", "Content", 12345L, Mood.HAPPY.name)
        coEvery { dao.updateEntry(entityEntry) } returns Unit

        repository.updateEntry(domainEntry)

        coVerify(exactly = 1) { dao.updateEntry(entityEntry) }
    }

    @Test
    fun `getEntryStream returns correct entry`() = runTest {
        val entityEntry = JournalEntryEntity(1, "Title", "Content", 12345L, Mood.HAPPY.name)
        val domainEntry = JournalEntry(1, "Title", "Content", 12345L, Mood.HAPPY)
        coEvery { dao.getEntryById(1) } returns entityEntry

        val result = repository.getEntryStream(1)

        assertEquals(domainEntry, result)
        coVerify(exactly = 1) { dao.getEntryById(1) }
    }

    @Test
    fun `getAllEntriesStream returns all entries`() = runTest {
        val entityEntries = listOf(
            JournalEntryEntity(1, "Title 1", "Content 1", 12345L, Mood.HAPPY.name),
            JournalEntryEntity(2, "Title 2", "Content 2", 12346L, Mood.CALM.name)
        )
        val domainEntries = listOf(
            JournalEntry(1, "Title 1", "Content 1", 12345L, Mood.HAPPY),
            JournalEntry(2, "Title 2", "Content 2", 12346L, Mood.CALM)
        )
        every { dao.getAllEntries() } returns flowOf(entityEntries)

        val result = repository.getAllEntriesStream().first()

        assertEquals(domainEntries, result)
        verify(exactly = 1) { dao.getAllEntries() }
    }
}
