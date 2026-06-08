package com.example.lumina

import android.app.Application
import com.example.lumina.data.JournalDatabase
import com.example.lumina.data.JournalRepository
import com.example.lumina.data.OfflineJournalRepository

class LuminaApplication : Application() {
    /**
     * AppContainer instance used by the rest of the classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val journalRepository: JournalRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineJournalRepository]
 */
class AppDataContainer(private val context: android.content.Context) : AppContainer {
    /**
     * Implementation for [JournalRepository]
     */
    override val journalRepository: JournalRepository by lazy {
        OfflineJournalRepository(JournalDatabase.getDatabase(context).journalDao())
    }
}
