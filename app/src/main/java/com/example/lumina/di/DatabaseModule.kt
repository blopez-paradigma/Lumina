package com.example.lumina.di

import android.content.Context
import com.example.lumina.data.JournalDao
import com.example.lumina.data.JournalDatabase
import com.example.lumina.data.JournalRepository
import com.example.lumina.data.OfflineJournalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JournalDatabase {
        return JournalDatabase.getDatabase(context)
    }

    @Provides
    fun provideJournalDao(database: JournalDatabase): JournalDao {
        return database.journalDao()
    }

    @Provides
    @Singleton
    fun provideJournalRepository(journalDao: JournalDao): JournalRepository {
        return OfflineJournalRepository(journalDao)
    }
}
