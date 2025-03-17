package com.example.diplomaproject

import android.app.AlertDialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity

class NotificationActivity: ComponentActivity() {
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.attack_message_title)
            .setMessage(getString(R.string.attack_message) + ":\n\n${intent.extras?.getString("INPUT_DATA")}")
            .setCancelable(true)
            .setNeutralButton(R.string.attack_message_button, { dialog, _  ->
                dialog.cancel()
                finish()
            }).create().apply {
                show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
        alertDialog = null
    }
}