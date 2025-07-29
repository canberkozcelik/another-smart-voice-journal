package com.example.anothersmartvoicejournal.core.data.dao

import androidx.room.*
import com.example.anothersmartvoicejournal.core.data.entity.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE isDraft = 0 ORDER BY createdAt DESC")
    fun getPublishedEntries(): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: String): JournalEntry?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry)
    
    @Update
    suspend fun updateEntry(entry: JournalEntry)
    
    @Delete
    suspend fun deleteEntry(entry: JournalEntry)
    
    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntryById(id: String)
    
    @Query("SELECT * FROM journal_entries WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchEntries(query: String): Flow<List<JournalEntry>>
    
    @Query("SELECT COUNT(*) FROM journal_entries WHERE isDraft = 0")
    fun getEntryCount(): Flow<Int>
} 