package com.example.diplomaproject

import android.content.Context

object DataProvider {
    fun getUrls(context: Context): List<String> {
        val fileInputStream = context.assets.open("auto_test_urls.txt")
        val content = fileInputStream
            .bufferedReader()
            .readLines()
            .map { "http://$it" }

        fileInputStream.close()
        return content
    }
}