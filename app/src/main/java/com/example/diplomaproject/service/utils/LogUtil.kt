package com.example.diplomaproject.service.utils

import android.util.Log

inline fun <reified T> log(block: () -> String?) {
    val text = block() ?: return
    if (text.isNotBlank()) Log.i(T::class.java.simpleName, "text: $text")
}