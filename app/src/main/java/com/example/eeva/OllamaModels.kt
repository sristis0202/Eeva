// OllamaModels.kt
package com.example.eeva // Use your actual package name

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false // Set to false for a single response
)

@Serializable
data class OllamaResponse(
    val model: String,
    @SerialName("created_at")
    val createdAt: String,
    val response: String,
    val done: Boolean,
    @SerialName("total_duration")
    val totalDuration: Long? = null, // Optional fields based on Ollama version/response
    @SerialName("load_duration")
    val loadDuration: Long? = null,
    @SerialName("prompt_eval_count")
    val promptEvalCount: Int? = null,
    @SerialName("prompt_eval_duration")
    val promptEvalDuration: Long? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    @SerialName("eval_duration")
    val evalDuration: Long? = null
    // Add other fields if needed, like 'context' for conversational history
)
