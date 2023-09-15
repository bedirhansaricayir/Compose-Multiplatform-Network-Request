package com.migren.composemultiplatform.model

import kotlinx.serialization.Serializable

@Serializable
data class BirdModel(
    val author: String,
    val category: String,
    val path: String
)