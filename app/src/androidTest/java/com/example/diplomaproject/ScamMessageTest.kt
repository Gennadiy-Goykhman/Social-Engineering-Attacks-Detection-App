package com.example.diplomaproject

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

//@RunWith(JUnit4::class)
class ScamMessageTest {
    //@get:Rule
    internal var activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val scamMessage = "Congratulations!!! You have won a \$1000 Amazon gift card! Click here to claim your prize now"
    private val browserPackage = "com.android.chrome"

//    @Test
//    fun scamMessageScenario(){
//        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
//
//        device.run {
//            setupApp()
//            openBrowser()
//            enterSpamText(scamMessage)
//            verifySpamWarning()
//        }
//    }

    private fun UiDevice.openBrowser() {
        // Запускаем браузер
        executeShellCommand("am start -n $browserPackage/com.google.android.apps.chrome.Main")
        wait(Until.gone(By.text("SEADA")), 5000)

        // Закрываем всплывающие подсказки при первом запуске
        try {
            val withoutButton = wait(
                Until.findObject(By.text("Use without an account")),
                2000
            )
            withoutButton?.click()

            val noThanksButton = wait(
                Until.findObject(By.text("No thanks")),
                2000
            )
            noThanksButton?.click()

            val gotItButton = wait(
                Until.findObject(By.text("Got it")),
                2000
            )
            gotItButton?.click()
        } catch (e: Exception) {
            // Игнорируем, если кнопки нет
        }
    }

    private fun UiDevice.enterSpamText(spamText: String) {
        // Находим адресную строку (может отличаться в разных версиях Chrome)
        val addressBar = wait(
            Until.findObject(By.res("$browserPackage:id/url_bar")),
            3000
        ) ?: wait(
            Until.findObject(By.res("$browserPackage:id/search_box_text")),
            3000
        )

        // Вводим текст
        addressBar?.click()
        addressBar?.text = spamText
        waitForIdle(2000)

        addressBar?.click(100)
        addressBar?.click(100)
        wait(Until.findObject(By.text("Copy")), 3000).click()
        waitForIdle(5000)

        addressBar?.click(100)
        addressBar?.click(100)
        wait(Until.findObject(By.text("Cut")), 3000).click()
        waitForIdle(5000)

        addressBar?.longClick()
        wait(Until.findObject(By.text("Paste")), 3000).click()
        waitForIdle(5000)

        // Нажимаем Enter
        // pressEnter()
        //waitForIdle(3000)
    }

    private fun UiDevice.verifySpamWarning() {
        val hasWarning = wait(Until.hasObject(By.text("Warning of a possible attack")), 20000)

        assertTrue(hasWarning)

        val okButton = wait(Until.findObject(By.text("OK")), 20000)
        okButton?.click()
        waitForIdle()
    }
}