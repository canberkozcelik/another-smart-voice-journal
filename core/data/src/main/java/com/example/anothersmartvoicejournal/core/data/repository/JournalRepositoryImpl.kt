package com.example.anothersmartvoicejournal.core.data.repository

import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.entity.JournalEntry as JournalEntryEntity
import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val journalDao: JournalDao
) : JournalRepository {
    
    override fun getAllEntries(): Flow<List<JournalEntry>> {
        return journalDao.getAllEntries().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getPublishedEntries(): Flow<List<JournalEntry>> {
        return journalDao.getPublishedEntries().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getEntryById(id: String): JournalEntry? {
        return journalDao.getEntryById(id)?.toDomainModel()
    }
    
    override suspend fun saveEntry(entry: JournalEntry): Result<Unit> {
        return try {
            journalDao.insertEntry(entry.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteEntry(id: String): Result<Unit> {
        return try {
            journalDao.deleteEntryById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchEntries(query: String): Flow<List<JournalEntry>> {
        return journalDao.searchEntries(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getEntryCount(): Flow<Int> {
        return journalDao.getEntryCount()
    }
    
    private fun JournalEntryEntity.toDomainModel(): JournalEntry {
        return JournalEntry(
            id = id,
            title = title,
            content = content,
            audioFilePath = audioFilePath,
            createdAt = createdAt,
            updatedAt = updatedAt,
            duration = duration,
            language = language,
            transcriptionConfidence = transcriptionConfidence,
            isDraft = isDraft
        )
    }
    
    private fun JournalEntry.toEntity(): JournalEntryEntity {
        return JournalEntryEntity(
            id = id,
            title = title,
            content = content,
            audioFilePath = audioFilePath,
            createdAt = createdAt,
            updatedAt = updatedAt,
            duration = duration,
            language = language,
            transcriptionConfidence = transcriptionConfidence,
            isDraft = isDraft
        )
    }
} 