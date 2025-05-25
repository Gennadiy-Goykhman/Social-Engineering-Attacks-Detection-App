package com.example.diplomaproject

import android.content.Intent
import android.content.res.AssetManager
import android.util.Log
import com.example.diplomaproject.data.model.DetectionResult
import com.example.diplomaproject.data.services.url.UrlDetectionService
import com.example.diplomaproject.service.AnalyzingForegroundService
import com.example.diplomaproject.service.detection.DetectionForegroundService
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

class DetectionForegroundServiceTest {
    private companion object {
        const val PHISHING_URL = "https://paypa1.com"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `CALL UrlDetectionService#detect WHEN event with phishing url received`() = runTest{
        mockkStatic(Log::class)
        mockkObject(UrlDetectionService)

        every { Log.e(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { UrlDetectionService.detect(any(), any(), any()) } returns Result.success(
            DetectionResult(1f)
        )

        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))

        val service = DetectionForegroundService().apply {
            setupCoroutineScope(this@runTest)
            setupProcessingDispatcher(UnconfinedTestDispatcher(testScheduler))
        }

        val serviceSpyk = spyk(service as AnalyzingForegroundService<String>) {
            every { this@spyk["prepareForeground"]() } returns Unit
            every { this@spyk["createNotificationChannel"]() } returns Unit
            every { getString(any()) } returns ""
            every { stopSelf() } returns Unit
            every { assets } returns mockk<AssetManager>(relaxed = true)
        }


        val mockIntent = mockk<Intent>(relaxed = true) {
           every { data.toString() } returns PHISHING_URL
        }

        serviceSpyk.onStartCommand(mockIntent, 0, 0)
        advanceUntilIdle()

        verify {
            UrlDetectionService.detect(
                model = any(),
                data = any(),
                context = any()
            )
        }

        Dispatchers.resetMain()

        unmockkStatic(Log::class)
        unmockkObject(UrlDetectionService)
    }
}