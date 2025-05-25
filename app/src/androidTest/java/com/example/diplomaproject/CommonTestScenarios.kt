package com.example.diplomaproject

import androidx.test.core.app.ActivityScenario
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

fun UiDevice.setupApp() {
    enableAccessibility()
    acceptPermissions()
    setupDefaultBrowser()
    returnToApp()
}

private fun UiDevice.enableAccessibility() {
    val service = findObject(By.text("Detection Service"))
    service?.click()
    waitForIdle()

    wait(Until.gone(By.text("Detection Service")), 20000)

    val toggle = findObject(By.textContains("Use"))
    toggle?.click()
    waitForIdle()

    wait(Until.gone(By.textContains("Use")), 20000)

    val allowButton = findObject(By.text("Allow"))
    allowButton?.click()
    waitForIdle()
}

private fun UiDevice.acceptPermissions() {
    val installedApps = findObject(By.text("Installed apps"))
    installedApps?.click()
    waitForIdle()

    ActivityScenario.launch(MainActivity::class.java)
    wait(Until.findObject(By.textContains("SEADA")), 20000)

    repeat(3) { // Обычно нужно принять несколько разрешений
        val allowBtn = wait(Until.findObject(By.text("Allow")), 5000)
        allowBtn?.click()
        waitForIdle(5000)

        val allowUsingBtn = wait(Until.findObject(By.textContains("While using")), 5000)
        allowUsingBtn?.click()
        waitForIdle(5000)
    }
}

private fun UiDevice.setupDefaultBrowser() {
    executeShellCommand("am start -a android.settings.MANAGE_DEFAULT_APPS_SETTINGS")
    val browserApp = wait(Until.findObject(By.text("Browser app")), 20000)
    browserApp?.click()
    waitForIdle()

    val seada = wait(Until.findObject(By.text("SEADA")), 20000)
    seada?.click()
    waitForIdle(5000)
}

private fun UiDevice.returnToApp() {
    ActivityScenario.launch(MainActivity::class.java)
    wait(Until.hasObject(By.textContains("SEADA")), 20000)

}