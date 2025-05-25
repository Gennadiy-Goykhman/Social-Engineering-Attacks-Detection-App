package com.example.diplomaproject

import android.util.Log
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PhishingLinkTest {

    @get:Rule
    internal var activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val phishingUrls = mutableListOf<String>()

    @get:Rule
    internal val logger
        get() = object : TestWatcher() {
            override fun finished(description: Description?) {
                super.finished(description)
                Log.d(
                    PhishingLinkTest::class.simpleName, """
            |Phishing link testing
            |======== FINAL REPORT ========
            |Total tests: ${phishingUrls.size}
            |Passed: $passedTests
            |Failed: ${phishingUrls.size - passedTests}
            |Success rate: ${(passedTests / phishingUrls.size.toFloat()) * 100}%
        """.trimIndent()
                )
            }
        }

    private var passedTests = 0

    @Test
    fun phishingLinkScenario(){
        val instr = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instr)

        device.run {
            setupApp()
            phishingUrls.addAll(DataProvider.getUrls(instr.targetContext))
            phishingUrls.forEach {
                openPhishingLink(it)
            }
        }
    }

//    @After
//    fun logTestResults() {
//        Log.d(PhishingLinkTest::class.simpleName, """
//            |Phishing link testing
//            |======== FINAL REPORT ========
//            |Total tests: ${phishingUrls.size}
//            |Passed: $passedTests
//            |Failed: ${phishingUrls.size - passedTests}
//            |Success rate: ${(passedTests / phishingUrls.size.toFloat()) * 100}%
//        """.trimIndent())
//    }

    private fun UiDevice.openPhishingLink(url: String) {
        executeShellCommand("am start -a android.intent.action.VIEW -d $url")
        val hasWarning = wait(Until.hasObject(By.text("Warning of a possible attack")), 3000)

        if (hasWarning) passedTests++

        val okButton = wait(Until.findObject(By.text("OK")), 3000)
        okButton?.click()
        waitForIdle()
    }
}

