// MainActivity.kt
package com.example.eeva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eeva.ui.theme.EevaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EevaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OllamaInteractionScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun OllamaInteractionScreen(
    modifier: Modifier = Modifier,
    ollamaViewModel: OllamaViewModel = viewModel() // Get instance of ViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Chat with Ollama (${ollamaViewModel.isLoading.let { if(it) "Loading model..." else "Ready" }})", // Show which model is targeted
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = ollamaViewModel.promptText,
            onValueChange = { ollamaViewModel.updatePromptText(it) },
            label = { Text("Enter your prompt") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = { ollamaViewModel.sendPromptToOllama() },
            enabled = !ollamaViewModel.isLoading && ollamaViewModel.promptText.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (ollamaViewModel.isLoading) "Sending..." else "Send to Ollama")
        }

        Text(
            text = "Ollama's Response:",
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Takes remaining space
                .padding(8.dp)
                .verticalScroll(rememberScrollState()) // Make response scrollable
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                .padding(8.dp)
        ) {
            Text(
                text = ollamaViewModel.ollamaResponse,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OllamaInteractionScreenPreview() {
    EevaTheme {
        // You can't directly preview the ViewModel interactions easily
        // But you can preview the layout
        OllamaInteractionScreen()
    }
}
