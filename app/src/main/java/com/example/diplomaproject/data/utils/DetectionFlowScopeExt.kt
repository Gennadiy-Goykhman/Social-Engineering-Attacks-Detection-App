package com.example.diplomaproject.data.utils

import android.content.Context
import android.util.Log
import com.example.diplomaproject.data.DataPreparationService
import com.example.diplomaproject.data.DetectionService
import com.example.diplomaproject.data.exception.EmptyDataException
import com.example.diplomaproject.data.exception.UnableToDetectResultException

fun DetectionFlowScope.prepareData(features: FloatArray) {
    detectionContext.data = DataPreparationService.prepareData(features)
}

fun DetectionFlowScope.detect(context: Context) {
    detectionContext.result = DetectionService.detect(
        data = detectionContext.data ?: throw EmptyDataException("Data for detection was not provided"),
        context = context
    ).getOrNull() ?: throw UnableToDetectResultException("Something go wrong while detecting")
}

fun DetectionFlowScope.notifyResult() {
    Log.i("DETECTION_FLOW_SCOPE", "Result is ${detectionContext.result?.result}")
}

fun detectionScopeBuilder() = object : DetectionFlowScope {
    override val detectionContext: DetectionFlowContext = DetectionFlowContext()
}

inline fun detectionScope(actions: DetectionFlowScope.() -> Unit) {
    val scope = detectionScopeBuilder()
    scope.actions()
}