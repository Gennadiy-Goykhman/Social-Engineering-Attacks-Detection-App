package com.example.diplomaproject.data.services.url

import java.net.URL

interface UrlFeatureExtruder<out T> {
    fun extrude(url: URL): T
}