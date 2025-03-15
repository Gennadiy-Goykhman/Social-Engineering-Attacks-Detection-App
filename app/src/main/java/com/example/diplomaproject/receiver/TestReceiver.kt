package com.example.diplomaproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.diplomaproject.MainActivity
import com.example.diplomaproject.data.utils.detect
import com.example.diplomaproject.data.utils.detectionScope
import com.example.diplomaproject.data.utils.notifyResult
import com.example.diplomaproject.data.utils.prepareData
import com.example.diplomaproject.service.TestForegroundService

class TestReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = Unit
}