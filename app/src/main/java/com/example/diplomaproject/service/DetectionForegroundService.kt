package com.example.diplomaproject.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.diplomaproject.R
import com.example.diplomaproject.data.services.url.UrlClassifierV2FeaturesExtruder
import com.example.diplomaproject.data.utils.detect
import com.example.diplomaproject.data.utils.detectionScope
import com.example.diplomaproject.data.utils.getFeaturesParams
import com.example.diplomaproject.data.utils.notifyResult
import com.example.diplomaproject.data.utils.prepareData
import com.example.diplomaproject.data.utils.retrieveUrl
import com.example.diplomaproject.service.utils.log
import java.net.URL


class DetectionForegroundService: Service() {
    private val channelId = "${DetectionAccessibilityService::class.simpleName}Channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_REDELIVER_INTENT
        prepareForeground()
        analyzeUrl { intent.data.toString() }
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun prepareForeground() {
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.detection_service_notification_title))
            .setContentText(getString(R.string.detection_service_notification_description))
            .setSmallIcon(R.drawable.ic_shield_default)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private inline fun analyzeUrl(textProvider: () -> String) {
        val url = retrieveUrl(textProvider)
        val features = url.getFeaturesParams(UrlClassifierV2FeaturesExtruder)
        detect(url, this, features)
        log<DetectionAccessibilityService>(textProvider)
    }

    private fun detect(url: URL, context: Context, features: LongArray) {
        detectionScope {
            prepareData(features)
            detect(context)
            notifyResult(url, context) {
                stopSelf()
            }
        }
    }
}