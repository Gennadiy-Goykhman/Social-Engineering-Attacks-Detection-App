package com.example.diplomaproject.data.services.url

import com.example.diplomaproject.data.utils.countSymbol
import java.net.URL

object UrlClassifierV2FeaturesExtruder: UrlFeatureExtruder<LongArray> {

    private val tokens = listOf("com", "https:",
        "www", "html", "http:", "org", "net", "cn", "php", "index", "htm",
        "co", "en", "login", "jp", "wiki", "ca", "wikipedia", "uk", "duckdns")

    private val dotsCount: URL.() -> Long = { countSymbol { '.' }.toLong() }
    private val barCount: URL.() -> Long = { countSymbol { '/' }.toLong() }
    private val lengthDetector: URL.() -> Long = { this.toString().length.toLong() }
    private val digitsCount: URL.() -> Long = { (0..9).sumOf { countSymbol { it.digitToChar() } }.toLong() }

    private val featuresDetectors = buildList<URL.() -> Long> {
        addAll(tokens.map { token -> { contains { token }.toLong() } })
        add(dotsCount)
        add(barCount)
        add(lengthDetector)
        add(digitsCount)
    }

    override fun extrude(url: URL): LongArray{
        return featuresDetectors.map { detector -> url.detector() }.toLongArray()
    }

    private inline fun URL.contains(stringProvider: () -> String): Int =
        if (toString().contains(stringProvider())) 1 else 0
}