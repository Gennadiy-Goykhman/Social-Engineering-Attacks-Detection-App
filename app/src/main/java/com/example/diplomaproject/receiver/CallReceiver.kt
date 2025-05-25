package com.example.diplomaproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.example.diplomaproject.service.call.PhoneCallsForegroundService
import com.example.diplomaproject.service.utils.AnalyzingForegroundStarter
import com.example.diplomaproject.service.utils.logi
import com.example.diplomaproject.service.utils.provideForegroundStarter

class CallReceiver:
    BroadcastReceiver(),
    AnalyzingForegroundStarter by provideForegroundStarter<PhoneCallsForegroundService>()
{
    private var currentNumber: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            TelephonyManager.EXTRA_STATE
            when (stateStr) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    currentNumber = incomingNumber
                    logi<CallReceiver> { "Звонит номер: $incomingNumber" }
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    if (incomingNumber == null) return
                    logi<CallReceiver> { "Звонок принят от $incomingNumber" }
                    intent.putExtra("CALLER_PHONE_NUMBER", incomingNumber)
                    context.launchAnalyze(intent)
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    currentNumber = null
                    logi<CallReceiver> { "Телефон в режиме ожидания" }
                }
            }
        }
    }
}