package com.domain.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisteredContactsResponse (
    val name: String? = "",
    val image: String? = "",
    val contactId: Int? = 0,
    val contactType: String? = "",
)