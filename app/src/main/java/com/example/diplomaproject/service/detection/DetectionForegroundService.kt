package com.example.diplomaproject.service.detection

import android.content.Context
import android.content.Intent
import com.example.diplomaproject.R
import com.example.diplomaproject.data.services.url.UrlClassifierV2FeaturesExtruder
import com.example.diplomaproject.data.utils.detect
import com.example.diplomaproject.data.utils.detectionScope
import com.example.diplomaproject.data.utils.getFeaturesParams
import com.example.diplomaproject.data.utils.notifyResult
import com.example.diplomaproject.data.utils.prepareData
import com.example.diplomaproject.data.utils.retrieveUrl
import com.example.diplomaproject.service.AnalyzingForegroundService
import com.example.diplomaproject.service.utils.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class DetectionForegroundService : AnalyzingForegroundService<String>() {
    override val channelId = "${DetectionAccessibilityService::class.simpleName}Channel"
    override val title get() = getString(R.string.detection_service_notification_title)
    override val description get() = getString(R.string.detection_service_notification_description)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun Intent.prepareForAnalyzing(): String = data.toString()

    override fun analyze(dataProvider: () -> String) {
        val url = retrieveUrl(dataProvider)
        val features = url.getFeaturesParams(UrlClassifierV2FeaturesExtruder)
        detect(url, this, features)
        logi<DetectionAccessibilityService>(dataProvider)
    }

    private fun detect(url: URL, context: Context, features: LongArray) {
        coroutineScope.launch(SupervisorJob()) {
            detectionScope {
                delay(2000)
                prepareData(features)
                detect(context)
                withContext(Dispatchers.Main) {
                    notifyResult(url, context, relaxTimeMillis = 0) {
                        stopSelf()
                    }
                }
            }
        }
    }
}