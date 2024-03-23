package com.domain.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenericResponse<T>(
    val message: String? = "",
    val timestamp: String? = "",
    val hasErrors: Boolean? = false,
    val result: T? = null,

)