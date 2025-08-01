package com.example.anothersmartvoicejournal.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.dao.SummaryDao
import com.example.anothersmartvoicejournal.core.data.entity.JournalEntry
import com.example.anothersmartvoicejournal.core.data.entity.Summary

@Database(
    entities = [JournalEntry::class, Summary::class],
    version = 1,
    exportSchema = false
)
abstract class VoiceJournalDatabase : RoomDatabase() {

    abstract fun journalDao(): JournalDao
    abstract fun summaryDao(): SummaryDao

    companion object {
        @Volatile
        private var INSTANCE: VoiceJournalDatabase? = null

        fun getDatabase(context: Context): VoiceJournalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VoiceJournalDatabase::class.java,
                    "voice_journal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
