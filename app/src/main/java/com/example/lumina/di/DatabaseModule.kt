package com.example.lumina.di

import android.content.Context
import com.example.lumina.data.datasource.LocalJournalDataSource
import com.example.lumina.data.datasource.RemoteJournalDataSource
import com.example.lumina.data.local.JournalDao
import com.example.lumina.data.local.JournalDatabase
import com.example.lumina.data.repository.SyncedJournalRepository
import com.example.lumina.domain.repository.JournalRepository
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
    fun provideJournalRepository(
        localDataSource: LocalJournalDataSource,
        remoteDataSource: RemoteJournalDataSource
    ): JournalRepository {
        return SyncedJournalRepository(localDataSource, remoteDataSource)
    }
}
