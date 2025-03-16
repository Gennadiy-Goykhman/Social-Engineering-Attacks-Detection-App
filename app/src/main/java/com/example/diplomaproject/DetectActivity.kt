package com.example.diplomaproject

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.diplomaproject.service.DetectionForegroundService

internal class DetectActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAnalyzing()
        openBrowser()
    }

    private fun startAnalyzing() {
        val startIntent = Intent(this, DetectionForegroundService::class.java).apply {
            data = intent.data
        }
        startForegroundService(startIntent)
    }

    private fun openBrowser() {
        val openBrowserIntent = Intent().apply {
            this.action = intent.action
            this.data = intent.data
            this.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        }

        val activities = packageManager.queryIntentActivities(openBrowserIntent, PackageManager.MATCH_ALL)
        val activityInfo = activities.first().activityInfo

        openBrowserIntent.component = ComponentName(activityInfo.packageName, activityInfo.name)
        startActivity(openBrowserIntent)
        finishAfterTransition()
    }
}