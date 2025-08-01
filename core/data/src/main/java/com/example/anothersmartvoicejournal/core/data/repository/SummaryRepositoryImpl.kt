package com.example.anothersmartvoicejournal.core.data.repository

import com.example.anothersmartvoicejournal.core.data.dao.SummaryDao
import com.example.anothersmartvoicejournal.core.data.entity.Summary as SummaryEntity
import com.example.anothersmartvoicejournal.core.domain.model.Summary
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SummaryRepositoryImpl @Inject constructor(
    private val summaryDao: SummaryDao
) : SummaryRepository {

    override fun getSummariesForEntry(entryId: String): Flow<List<Summary>> {
        return summaryDao.getSummariesForEntry(entryId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getSummaryById(id: String): Summary? {
        return summaryDao.getSummaryById(id)?.toDomainModel()
    }

    override suspend fun saveSummary(summary: Summary): Result<Unit> {
        return try {
            summaryDao.insertSummary(summary.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSummary(id: String): Result<Unit> {
        return try {
            summaryDao.getSummaryById(id)?.let { summaryDao.deleteSummary(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSummariesForEntry(entryId: String): Result<Unit> {
        return try {
            summaryDao.deleteSummariesForEntry(entryId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getRecentSummaries(limit: Int): Flow<List<Summary>> {
        return summaryDao.getRecentSummaries(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    private fun SummaryEntity.toDomainModel(): Summary {
        return Summary(
            id = id,
            entryId = entryId,
            content = content,
            bulletPoints = bulletPoints,
            inputType = inputType,
            createdAt = createdAt,
            confidence = confidence
        )
    }

    private fun Summary.toEntity(): SummaryEntity {
        return SummaryEntity(
            id = id,
            entryId = entryId,
            content = content,
            bulletPoints = bulletPoints,
            inputType = inputType,
            createdAt = createdAt,
            confidence = confidence
        )
    }
}
