package com.example.diplomaproject.data.utils

import java.net.URL

interface UrlFeatureExtruder {
    fun extrude(url: URL): FloatArray
}

object UrlClassifierFeaturesExtruder: UrlFeatureExtruder {
    private val lengthDetector: URL.() -> Float = { this.toString().length.toFloat() }
    private val dotsCount: URL.() -> Float = { countSymbolF { '.' } }
    private val hyphenCount: URL.() -> Float = { countSymbolF { '-' } }
    private val underlineCount: URL.() -> Float = { countSymbolF { '_' } }
    private val slashCount: URL.() -> Float = { countSymbolF { '/' } }
    private val questionMarkCount: URL.() -> Float = { countSymbolF { '?' } }
    private val equalCount: URL.() -> Float = { countSymbolF { '=' } }
    private val atCount: URL.() -> Float = { countSymbolF { '=' } }
    private val andCount: URL.() -> Float = { countSymbolF { '&' } }
    private val exclamationCount: URL.() -> Float = { countSymbolF { '!' } }
    private val spaceCount: URL.() -> Float = { countSymbolF { ' ' } }
    private val tildaCount: URL.() -> Float = { countSymbolF { '~' } }
    private val commaCount: URL.() -> Float = { countSymbolF { ',' } }
    private val plusCount: URL.() -> Float = { countSymbolF { '+' } }
    private val asteriskCount: URL.() -> Float = { countSymbolF { '*' } }
    private val hashtagCount: URL.() -> Float = { countSymbolF { '#' } }
    private val dollarCount: URL.() -> Float = { countSymbolF { '$' } }
    private val percentCount: URL.() -> Float = { countSymbolF { '%' } }
    private val redirectionCount: URL.() -> Float = { 0f }

    private val featuresDetectors = listOf(
        lengthDetector,
        dotsCount,
        hyphenCount,
        underlineCount,
        slashCount,
        questionMarkCount,
        equalCount,
        atCount,
        andCount,
        exclamationCount,
        spaceCount,
        tildaCount,
        commaCount,
        plusCount,
        asteriskCount,
        hashtagCount,
        dollarCount,
        percentCount,
        redirectionCount
    )

    override fun extrude(url: URL): FloatArray {
        return featuresDetectors.map { detector -> url.detector() }.toFloatArray()
    }

    private inline fun URL.countSymbolF(symbolProvider: () -> Char): Float =
        countSymbol(symbolProvider).toFloat()
}