package com.example.diplomaproject.data.services.spam

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object VocabularyProvider {
    private const val VOCAB_FILE_NAME = "vocabulary.json"
    private val lock = ReentrantLock()
    private var _vocab: Map<String, Int>? = null

    fun getVocabulary(context: Context): Map<String, Int> = lock.withLock {
        return _vocab ?: loadVocabulary(context).also { _vocab = it }
    }

    private fun loadVocabulary(context: Context): Map<String, Int> {
        val fileStream = context.assets.open(VOCAB_FILE_NAME)
        val stringBuilder = StringBuilder()
        BufferedReader(InputStreamReader(fileStream)).use { bufferedReader ->
            var line: String?
            while ((bufferedReader.readLine().also { line = it }) != null) {
                stringBuilder.append(line).append("\n")
            }
        }

        val result = stringBuilder.toString().trim { it <= ' ' }
        val jsonObject = JSONObject(result)
        val map = buildMap<String, Int> {
            jsonObject.keys().forEach {
                this[it] = jsonObject.get(it).toString().toInt()
            }
        }

        return map
    }
}