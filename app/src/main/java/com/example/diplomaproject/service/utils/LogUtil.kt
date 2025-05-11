package com.example.diplomaproject.service.utils

import android.util.Log

inline fun <reified T> logi(block: () -> String?) {
    val text = block() ?: return
    if (text.isNotBlank()) Log.i(T::class.java.simpleName, "text: $text")
}

inline fun <reified T> loge(block: () -> String?) {
    val text = block() ?: return
    if (text.isNotBlank()) Log.e(T::class.java.simpleName, text)
}