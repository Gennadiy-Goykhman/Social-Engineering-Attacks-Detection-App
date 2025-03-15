package com.example.diplomaproject.data.utils

import com.example.diplomaproject.data.model.DetectionResult
import com.example.diplomaproject.data.model.PreparedData

interface DetectionFlowScope {
    val detectionContext: DetectionFlowContext
}

data class DetectionFlowContext(
    var data: PreparedData? = null,
    var result: DetectionResult<*>? = null
)