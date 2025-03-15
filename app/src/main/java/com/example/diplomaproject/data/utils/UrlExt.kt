package com.example.diplomaproject.data.utils

import java.net.URL

inline fun retrieveUrl(
    textBody: () -> String,
    pattern: String = """\b(?:https?:\/\/|www\.)\S+\b""",
): URL {
    return URL(Regex(pattern).find(textBody())?.value)
}

inline fun URL.countSymbol(symbolProvider: () -> Char) = this.toString()
    .count { it == symbolProvider() }


fun URL.getFeaturesParams(
    featuresExtruder: UrlFeatureExtruder = UrlClassifierFeaturesExtruder
): FloatArray {
    return featuresExtruder.extrude(this)
}