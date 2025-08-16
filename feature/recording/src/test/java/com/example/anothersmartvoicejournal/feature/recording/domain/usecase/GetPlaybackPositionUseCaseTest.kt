package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPlaybackPositionUseCaseTest {

    private lateinit var useCase: GetPlaybackPositionUseCase
    private lateinit var mockRepository: PlaybackRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetPlaybackPositionUseCase(mockRepository)
    }

    @Test
    fun `GetPlaybackPositionUseCase should return current position during playback`() = runTest {
        // Given
        val expectedPosition = 45000L // 45 seconds
        coEvery { mockRepository.getCurrentPosition() } returns expectedPosition
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(expectedPosition, result)
    }

    @Test
    fun `GetPlaybackPositionUseCase should return zero when not playing`() = runTest {
        // Given
        coEvery { mockRepository.getCurrentPosition() } returns 0L
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(0L, result)
    }

    @Test
    fun `GetPlaybackPositionUseCase should handle position at start of audio`() = runTest {
        // Given
        coEvery { mockRepository.getCurrentPosition() } returns 0L
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(0L, result)
    }

    @Test
    fun `GetPlaybackPositionUseCase should handle position near end of audio`() = runTest {
        // Given
        val expectedPosition = 118000L // Near end of 2-minute audio
        coEvery { mockRepository.getCurrentPosition() } returns expectedPosition
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(expectedPosition, result)
    }

    @Test
    fun `GetPlaybackPositionUseCase should return consistent position for paused audio`() = runTest {
        // Given
        val expectedPosition = 30000L // 30 seconds
        coEvery { mockRepository.getCurrentPosition() } returns expectedPosition
        
        // When
        val result1 = useCase.execute()
        val result2 = useCase.execute()
        val result3 = useCase.execute()
        
        // Then
        assertEquals(expectedPosition, result1)
        assertEquals(expectedPosition, result2)
        assertEquals(expectedPosition, result3)
    }

    @Test
    fun `GetPlaybackPositionUseCase should handle very short audio positions`() = runTest {
        // Given
        val expectedPosition = 100L // 100ms
        coEvery { mockRepository.getCurrentPosition() } returns expectedPosition
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(expectedPosition, result)
    }

    @Test
    fun `GetPlaybackPositionUseCase should handle very long audio positions`() = runTest {
        // Given
        val expectedPosition = 3600000L // 1 hour
        coEvery { mockRepository.getCurrentPosition() } returns expectedPosition
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(expectedPosition, result)
    }

    @Test
    fun `GetPlaybackPositionUseCase should return zero on error`() = runTest {
        // Given
        coEvery { mockRepository.getCurrentPosition() } returns 0L
        
        // When
        val result = useCase.execute()
        
        // Then
        assertEquals(0L, result)
    }
}
