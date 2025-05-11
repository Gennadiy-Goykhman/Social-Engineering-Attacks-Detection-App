package com.example.diplomaproject.service.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

fun interface AnalyzingForegroundStarter {
    fun Context.launchAnalyze(intent: Intent)
}

inline fun <reified T: Service> provideForegroundStarter() = AnalyzingForegroundStarter {
    val startIntent = Intent(this, T::class.java).apply {
        data = it.data
        putExtras(it)
    }
    ContextCompat.startForegroundService(this, startIntent)
}