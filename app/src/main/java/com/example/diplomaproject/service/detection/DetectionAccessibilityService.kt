package com.example.diplomaproject.service.detection

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.webkit.URLUtil
import android.widget.Toast
import com.example.diplomaproject.MainActivity
import com.example.diplomaproject.R
import com.example.diplomaproject.data.model.ModelVersions
import com.example.diplomaproject.data.services.url.UrlClassifierV2FeaturesExtruder
import com.example.diplomaproject.data.utils.detect
import com.example.diplomaproject.data.utils.detectionScope
import com.example.diplomaproject.data.utils.getFeaturesParams
import com.example.diplomaproject.data.utils.notifyResult
import com.example.diplomaproject.data.utils.prepareData
import com.example.diplomaproject.data.utils.retrieveUrl
import com.example.diplomaproject.service.utils.loge
import com.example.diplomaproject.service.utils.logi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.URL

internal class DetectionAccessibilityService : AccessibilityService() {
    private companion object {
        const val MIN_MESSAGE_LENGTH = 10
    }



    private val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, exception ->
        loge<DetectionAccessibilityService> {
            """Exception occurred ${exception.message} 
                |${exception.stackTraceToString()}
            """.trimMargin()
        }
    })

    override fun onServiceConnected() {
        super.onServiceConnected()
        startActivity(
            Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        notifyAboutBrowser()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.run {
            text.joinToString { "" }.processText()
            contentDescription?.toString()?.processText()

            source?.run {
                text?.toString()?.processText()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    containerTitle?.toString()?.processText()
                    stateDescription?.toString()?.processText()
                    collectionItemInfo?.rowTitle?.processText()
                    collectionItemInfo?.columnTitle?.processText()
                }
                contentDescription?.toString()?.processText()
                hintText?.toString()?.processText()
            }
        }
    }

    private fun notifyAboutBrowser() {
        Toast.makeText(this, getString(R.string.default_browser_notification), Toast.LENGTH_LONG).show()
    }

    private fun String.processText() = coroutineScope.launch(SupervisorJob()) {
        if (URLUtil.isValidUrl(this@processText)) {
            analyzeUrl(this@DetectionAccessibilityService) { this@processText }
        } else if (this@processText.length >= MIN_MESSAGE_LENGTH) {
            detectSpam(this@DetectionAccessibilityService, this@processText)
        }
        logi<DetectionAccessibilityService> { this@processText }
    }

    private suspend inline fun analyzeUrl(context: Context, textProvider: () -> String) {
        val url = retrieveUrl(textProvider)
        val features = url.getFeaturesParams(UrlClassifierV2FeaturesExtruder)
        detectPhishing(url, context, features)
        logi<DetectionAccessibilityService>(textProvider)
    }

    private suspend fun detectSpam(context: Context, text: String) {
        detectionScope {
            prepareData(context, text)
            detect(context, model = ModelVersions.SPAM_CLASSIFIER)
            notifyResult(text, context)
        }
    }

    private suspend fun detectPhishing(url: URL, context: Context, features: LongArray) {
        detectionScope {
            prepareData(features)
            detect(context)
            notifyResult(url, context)
        }
    }

    override fun onInterrupt() = Unit
}