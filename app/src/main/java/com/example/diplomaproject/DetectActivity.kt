package com.example.diplomaproject

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.diplomaproject.service.detection.DetectionForegroundService
import com.example.diplomaproject.service.utils.AnalyzingForegroundStarter
import com.example.diplomaproject.service.utils.provideForegroundStarter

internal class DetectActivity:
    ComponentActivity(),
    AnalyzingForegroundStarter by provideForegroundStarter<DetectionForegroundService>()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchAnalyze(intent)
        openBrowser()
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