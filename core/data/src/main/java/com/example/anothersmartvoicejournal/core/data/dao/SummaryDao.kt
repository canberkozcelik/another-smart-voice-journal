package com.example.anothersmartvoicejournal.core.data.dao

import androidx.room.*
import com.example.anothersmartvoicejournal.core.data.entity.Summary
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao {
    
    @Query("SELECT * FROM summaries WHERE entryId = :entryId ORDER BY createdAt DESC")
    fun getSummariesForEntry(entryId: String): Flow<List<Summary>>
    
    @Query("SELECT * FROM summaries WHERE id = :id")
    suspend fun getSummaryById(id: String): Summary?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: Summary)
    
    @Update
    suspend fun updateSummary(summary: Summary)
    
    @Delete
    suspend fun deleteSummary(summary: Summary)
    
    @Query("DELETE FROM summaries WHERE entryId = :entryId")
    suspend fun deleteSummariesForEntry(entryId: String)
    
    @Query("SELECT * FROM summaries ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSummaries(limit: Int = 10): Flow<List<Summary>>
} 