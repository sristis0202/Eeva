package com.example.eeva

import io.ktor.client.statement.HttpResponse

// OllamaViewModel.kt
// Use your actual package name

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class OllamaViewModel : ViewModel() {
    var promptText by mutableStateOf("")
        private set // Only ViewModel can change this directly

    var ollamaResponse by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Configure your desired model name here
    private val modelName = "gemma3n" // Or whatever model you have downloaded

    // Ktor HTTP Client
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Important for robustness
            })
        }
    }

    fun updatePromptText(newText: String) {
        promptText = newText
    }

    fun sendPromptToOllama() {
        if (promptText.isBlank() || isLoading) return

        viewModelScope.launch {
            isLoading = true
            ollamaResponse = "Loading..."
            try {
                val requestBody = OllamaRequest(model = modelName, prompt = promptText)
                val response: HttpResponse = client.post("http://10.0.2.2:11434/api/generate") { // 10.0.2.2 is localhost for Android Emulator
                    // val response: OllamaResponse = client.post("http://YOUR_LOCAL_IP_HERE:11434/api/generate") { // For physical device
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
                ollamaResponse = response.body()
            } catch (e: Exception) {
                ollamaResponse = "Error: ${e.message}"
                e.printStackTrace() // Log the error for debugging
            } finally {
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}
