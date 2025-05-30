package com.example.diplomaproject.data.services

import com.example.diplomaproject.data.model.PreparedData
import org.pytorch.IValue
import org.pytorch.Tensor

internal object DataPreparationService {
    fun prepareData(features: FloatArray): PreparedData {
        return PreparedData(
            arrayOf(
                IValue.from(Tensor.fromBlob(features, longArrayOf(1, features.size.toLong())))
            )
        )
    }

    fun prepareData(features: LongArray): PreparedData {
        return PreparedData(
            arrayOf(
                IValue.from(Tensor.fromBlob(features.map { it.toFloat() }.toFloatArray(),
                    longArrayOf(1, features.size.toLong())))
            )
        )
    }
}