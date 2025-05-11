package com.example.diplomaproject.data.utils

import com.example.diplomaproject.data.services.url.UrlFeatureExtruder
import java.net.URL

inline fun retrieveUrl(
    textBody: () -> String,
    pattern: String = """\b(?:https?:\/\/|www\.)\S+\b""",
): URL {
    return URL(Regex(pattern).find(textBody())?.value ?: "")
}

inline fun URL.countSymbol(symbolProvider: () -> Char) = this.toString()
    .count { it == symbolProvider() }


fun <T> URL.getFeaturesParams(
    featuresExtruder: UrlFeatureExtruder<T>
): T {
    return featuresExtruder.extrude(this)
}