package com.example.diplomaproject.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.diplomaproject.MainActivity

class TestAccessibilityService: AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
        startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        p0?.text?.let {
            val resultText = it.joinToString(" ")
            if (resultText.isNotBlank()) Log.i(TestAccessibilityService::class.java.simpleName, "text: $resultText")
        }

        p0?.contentDescription?.let {
            if (it.isNotBlank()) Log.i(TestAccessibilityService::class.java.simpleName, "content description: $it")
        }
    }

    override fun onInterrupt() {
        // TODO("Not yet implemented")
    }
}