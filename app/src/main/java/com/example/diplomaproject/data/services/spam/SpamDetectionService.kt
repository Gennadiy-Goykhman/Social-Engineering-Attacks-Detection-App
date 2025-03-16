package com.example.diplomaproject.data.services.spam

import android.content.Context
import android.util.Log
import com.example.diplomaproject.data.model.DetectionResult
import com.example.diplomaproject.data.model.ModelVersions
import com.example.diplomaproject.data.model.PreparedData
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.exp

object SpamDetectionService {
    private var _neuroModule: Module? = null
    private val lock: ReentrantLock = ReentrantLock()

    fun detect(model: ModelVersions, data: PreparedData, context: Context) = lock.withLock {
        return@withLock runCatching {
            val outputTensor = getModule(model, context).forward(*data.values)?.toTensor()
            val scores = outputTensor?.dataAsFloatArray ?: floatArrayOf()
            val sofMaxResult = softmax(scores)
            DetectionResult(sofMaxResult.get(1) ?: 0.0f)
        }.onFailure {
            Log.e("GH", it.toString())
        }
    }

    private fun softmax(input: FloatArray): FloatArray {
        val expValues = input.map { exp(it) }
        val sumExpValues = expValues.sum()
        return expValues.map { it / sumExpValues }.toFloatArray()
    }

    private fun getModule(model: ModelVersions, context: Context) =
        _neuroModule ?: initModule(model, context).also { _neuroModule = it }

    private fun initModule(model: ModelVersions, context: Context): Module {
        val inputStream = context.assets.open(model.path)
        val file = File(context.cacheDir, model.path)

        FileOutputStream(file).use { outputStream ->
            val buffer = ByteArray(4 * 1024)
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }

            outputStream.flush()
        }

        return LiteModuleLoader.load(file.absolutePath)
    }
}