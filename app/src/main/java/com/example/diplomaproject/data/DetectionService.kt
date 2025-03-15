package com.example.diplomaproject.data

import android.content.Context
import com.example.diplomaproject.data.model.DetectionResult
import com.example.diplomaproject.data.model.PreparedData
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal object DetectionService {
    private const val MODEL_NAME = "url_classifier_first_version.pk"
    private var _neuroModule: Module? = null
    private val lock: ReentrantLock = ReentrantLock()


    fun detect(data: PreparedData, context: Context) = lock.withLock {
        return@withLock runCatching {
            val outputTensor = getModule(context).forward(*data.values)?.toTensor()
            val scores = outputTensor?.dataAsFloatArray
            DetectionResult(scores?.get(0) ?: 0.0f)  // Возвращаем вероятность
        }
    }

    private fun getModule(context: Context) = _neuroModule ?: initModule(context)

    private fun initModule(context: Context): Module {
        val inputStream = context.assets.open(MODEL_NAME)
        val file = File(context.cacheDir, MODEL_NAME)

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