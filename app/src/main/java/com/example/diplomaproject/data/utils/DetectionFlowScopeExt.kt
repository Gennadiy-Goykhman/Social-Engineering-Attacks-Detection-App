package com.example.diplomaproject.data.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.diplomaproject.NotificationActivity
import com.example.diplomaproject.R
import com.example.diplomaproject.data.exception.EmptyDataException
import com.example.diplomaproject.data.exception.UnableToDetectResultException
import com.example.diplomaproject.data.model.DetectionResult
import com.example.diplomaproject.data.model.ModelVersions
import com.example.diplomaproject.data.services.DataPreparationService
import com.example.diplomaproject.data.services.spam.SpamDetectionService
import com.example.diplomaproject.data.services.spam.VocabularyProvider
import com.example.diplomaproject.data.services.url.UrlDetectionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun DetectionFlowScope.prepareData(context: Context, text: String) {
    val vocab = VocabularyProvider.getVocabulary(context)
    val features = TextVectorizer.vectorize(vocab, text)
    detectionContext.data = DataPreparationService.prepareData(features)
}

fun DetectionFlowScope.prepareData(features: FloatArray) {
    detectionContext.data = DataPreparationService.prepareData(features)
}

fun DetectionFlowScope.prepareData(features: LongArray) {
    detectionContext.data = DataPreparationService.prepareData(features)
}

fun DetectionFlowScope.detect(
    context: Context,
    model: ModelVersions = ModelVersions.URL_CLASSIFIER_V2
) {
    detectionContext.result = when (model) {
        ModelVersions.URL_CLASSIFIER_V1,
        ModelVersions.URL_CLASSIFIER_V2 -> {
            UrlDetectionService.detect(
                model = model,
                data = detectionContext.data
                    ?: throw EmptyDataException("Data for detection was not provided"),
                context = context
            ).getOrNull()
                ?: throw UnableToDetectResultException("Something go wrong while detecting")
        }

        ModelVersions.SPAM_CLASSIFIER -> {
            SpamDetectionService.detect(
                model = model,
                data = detectionContext.data
                    ?: throw EmptyDataException("Data for detection was not provided"),
                context = context
            ).getOrNull()
                ?: throw UnableToDetectResultException("Something go wrong while detecting")
        }
    }.prepareResult(model)
}

fun <T> DetectionFlowScope.notifyResultWithToast(data: T, context: Context, onNotify: (() -> Unit)? = null) {
    val result = detectionContext.result?.result as? Float ?: return
    if (result < 0.5) {
        Log.i(
            "DETECTION_FLOW_SCOPE",
            "Result is ${detectionContext.result?.result}. It is not critical value"
        )
        onNotify?.invoke()
        return
    }

    val message = ContextCompat.getString(context, R.string.attack_message) + ": ${data.toString()}"
    Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    onNotify?.invoke()
}

private val notifyMutex = Mutex(false)

suspend fun <T> DetectionFlowScope.notifyResult(
    data: T,
    context: Context,
    relaxTimeMillis: Long = 3000,
    onNotify: (() -> Unit)? = null
) {
    notifyMutex.withLock {
        val result = detectionContext.result?.result as? Float ?: return
        if (result < 0.5) {
            Log.i(
                "DETECTION_FLOW_SCOPE",
                "Result is ${detectionContext.result?.result}. It is not critical value"
            )
            onNotify?.invoke()
            return
        }

        withContext(Dispatchers.Main) {
            val intent = Intent(context, NotificationActivity::class.java).apply {
                val bundle = bundleOf("INPUT_DATA" to data.toString())
                putExtras(bundle)
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }

            ContextCompat.startActivity(context, intent, null)
            onNotify?.invoke()
        }

        delay(relaxTimeMillis)
    }
}

fun detectionScopeBuilder() = object : DetectionFlowScope {
    override val detectionContext: DetectionFlowContext = DetectionFlowContext()
}

suspend inline fun detectionScope(
    withCoroutineContext: CoroutineContext =  Dispatchers.Default,
    crossinline actions: suspend DetectionFlowScope.() -> Unit,
) {
    val scope = detectionScopeBuilder()
    withContext(withCoroutineContext) {
        scope.actions()
    }
}

private fun DetectionResult<Float>.prepareResult(model: ModelVersions): DetectionResult<Float> {
    return when (model) {
        ModelVersions.URL_CLASSIFIER_V1,
        ModelVersions.SPAM_CLASSIFIER -> this
        ModelVersions.URL_CLASSIFIER_V2 -> DetectionResult(1 - result)
    }
}