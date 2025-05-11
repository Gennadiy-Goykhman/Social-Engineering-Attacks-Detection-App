package com.example.diplomaproject.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.diplomaproject.R
import kotlin.random.Random

abstract class AnalyzingForegroundService<T>: Service() {
    abstract val channelId: String
    abstract val title: String
    abstract val description: String

    private val serviceId = Random(Int.MAX_VALUE).nextInt()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(p0: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_REDELIVER_INTENT
        prepareForeground()
        analyze { intent.prepareForAnalyzing() }
        return START_STICKY
    }

    protected abstract fun Intent.prepareForAnalyzing(): T
    protected abstract fun analyze(dataProvider: () -> T)

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun prepareForeground() {
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_shield_default)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(serviceId, notification)
    }
}