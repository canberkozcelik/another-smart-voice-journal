package com.example.anothersmartvoicejournal.core.data.di

import android.content.Context
import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.dao.SummaryDao
import com.example.anothersmartvoicejournal.core.data.database.VoiceJournalDatabase
import com.example.anothersmartvoicejournal.core.data.repository.JournalRepositoryImpl
import com.example.anothersmartvoicejournal.core.data.repository.SummaryRepositoryImpl
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
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
    fun provideDatabase(@ApplicationContext context: Context): VoiceJournalDatabase {
        return VoiceJournalDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideJournalDao(database: VoiceJournalDatabase): JournalDao {
        return database.journalDao()
    }
    
    @Provides
    fun provideSummaryDao(database: VoiceJournalDatabase): SummaryDao {
        return database.summaryDao()
    }
    
    @Provides
    @Singleton
    fun provideJournalRepository(impl: JournalRepositoryImpl): JournalRepository {
        return impl
    }
    
    @Provides
    @Singleton
    fun provideSummaryRepository(impl: SummaryRepositoryImpl): SummaryRepository {
        return impl
    }
} 