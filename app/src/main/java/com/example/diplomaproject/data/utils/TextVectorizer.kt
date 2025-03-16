package com.example.diplomaproject.data.utils

object TextVectorizer {
    fun vectorize(vocabulary: Map<String, Int>, text: String): FloatArray {
        val words = text.lowercase().split("\\s+".toRegex())
        val vector = FloatArray(vocabulary.size)

        for (word in words) {
            vocabulary[word]?.let { index ->
                vector[index]++
            }
        }

        return vector
    }
}