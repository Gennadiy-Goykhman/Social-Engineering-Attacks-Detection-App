package com.example.diplomaproject.data.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class InputData<T>(val data: T)