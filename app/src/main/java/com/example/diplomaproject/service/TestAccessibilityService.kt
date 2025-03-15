package com.example.diplomaproject.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.webkit.URLUtil
import com.example.diplomaproject.MainActivity
import com.example.diplomaproject.data.utils.detect
import com.example.diplomaproject.data.utils.detectionScope
import com.example.diplomaproject.data.utils.getFeaturesParams
import com.example.diplomaproject.data.utils.notifyResult
import com.example.diplomaproject.data.utils.prepareData
import com.example.diplomaproject.data.utils.retrieveUrl
import java.net.URL

internal class TestAccessibilityService : AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
        startActivity(
            Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.run {
            text.joinToString { "" }.processText()

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

            contentDescription?.toString()?.processText()
        }
    }

    private fun String.processText() {
        if (URLUtil.isValidUrl(this)) analyzeUrl(this@TestAccessibilityService) { this }
        log { this }
    }

    private inline fun analyzeUrl(context: Context, textProvider: () -> String) {
        val url = retrieveUrl(textProvider)
        val features = url.getFeaturesParams()
        detect(context, features)
        log(textProvider)
    }

    private fun detect(context: Context, features: FloatArray) {
        detectionScope {
            prepareData(features)
            detect(context)
            notifyResult()
        }
    }

    private inline fun log(block: () -> String?) {
        val text = block() ?: return
        if (text.isNotBlank()) Log.i(TestAccessibilityService::class.java.simpleName, "text: $text")
    }

    override fun onInterrupt() = Unit
}