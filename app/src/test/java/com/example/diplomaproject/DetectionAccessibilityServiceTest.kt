package com.example.diplomaproject

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.webkit.URLUtil
import com.example.diplomaproject.data.model.DetectionResult
import com.example.diplomaproject.data.services.spam.SpamDetectionService
import com.example.diplomaproject.data.services.spam.VocabularyProvider
import com.example.diplomaproject.data.services.url.UrlDetectionService
import com.example.diplomaproject.service.detection.DetectionAccessibilityService
import io.mockk.coEvery
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
import org.junit.AfterClass
import org.junit.Test
import org.junit.Assert.*
import org.junit.BeforeClass
import java.net.URL

class DetectionAccessibilityServiceTest {
    private companion object {
        const val SCAM_MESSAGE = "Congratulations!!! You have won a \$1000 Amazon gift card! Click here to claim your prize now"
        const val NOT_PHISHING_URL = "https://www.google.com"

        @JvmStatic
        @BeforeClass
        fun `before tests preparation`(){
            mockkStatic(Log::class)
            mockkObject(SpamDetectionService)
            mockkObject(UrlDetectionService)
            mockkObject(VocabularyProvider)

            every { Log.e(any(), any()) } returns 0
            every { Log.i(any(), any()) } returns 0
            every { SpamDetectionService.detect(any(), any(), any()) } returns Result.success(DetectionResult(0f))
            every { VocabularyProvider.getVocabulary(any()) } returns mapOf()
            every { UrlDetectionService.detect(any(), any(), any()) } returns Result.success(DetectionResult(1f))
        }

        @JvmStatic
        @AfterClass
        fun `after tests finished`() {
            unmockkStatic(Log::class)
            unmockkObject(SpamDetectionService)
            unmockkObject(UrlDetectionService)
            unmockkObject(VocabularyProvider)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `CALL SpamDetectionService#detect WHEN event with spam message received`() = runTest {
        mockkStatic(URLUtil::class)
        every { URLUtil.isValidUrl(any()) } returns false

        val service = DetectionAccessibilityService().apply {
            setupCoroutineScope(this@runTest)
        }

        val serviceSpyk = spyk(service) {
            every { assets } returns mockk<AssetManager>(relaxed = true)
        }

        val mockEvent = mockk<AccessibilityEvent>(relaxed = true) {
            every { text } returns listOf(SCAM_MESSAGE)
        }

        var detectSpamCalled = false
        coEvery {  serviceSpyk["detectSpam"](any<Context>(), any<String>()) } answers {
            detectSpamCalled = true
            callOriginal()
        }

        serviceSpyk.onAccessibilityEvent(mockEvent)
        advanceUntilIdle()

        assertTrue(detectSpamCalled)

        verify {
            SpamDetectionService.detect(
                model = any(),
                data = any(),
                context = any()
            )
        }

        unmockkStatic(URLUtil::class)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `CALL UrlDetectionService#detect WHEN event with not phishing url received`() = runTest {
        mockkStatic(URLUtil::class)
        every { URLUtil.isValidUrl(any()) } returns true

        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))

        val service = DetectionAccessibilityService().apply {
            setupCoroutineScope(this@runTest)
        }

        val serviceSpyk = spyk(service) {
            every { getString(any()) } returns ""
            every { assets } returns mockk<AssetManager>(relaxed = true)
        }

        val mockEvent = mockk<AccessibilityEvent>(relaxed = true) {
            every { text } returns listOf(NOT_PHISHING_URL)
            every { contentDescription } returns null
            every { source } returns null
        }

        var detectPhishingCalled = false
        coEvery {  serviceSpyk["detectPhishing"](any<URL>(), any<Context>(), any<LongArray>()) } answers {
            detectPhishingCalled = true
            callOriginal()
        }

        serviceSpyk.onAccessibilityEvent(mockEvent)
        advanceUntilIdle()

        assertTrue(detectPhishingCalled)

        verify {
            UrlDetectionService.detect(
                model = any(),
                data = any(),
                context = any()
            )
        }

        unmockkStatic(URLUtil::class)
        Dispatchers.resetMain()
    }
}