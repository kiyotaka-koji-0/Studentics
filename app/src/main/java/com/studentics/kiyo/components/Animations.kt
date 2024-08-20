package com.studentics.kiyo.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.studentics.kiyo.utils.getFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.truncate


@Composable
fun TypewriterText(text: String) {
    var displayedText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(text) {
        displayedText = ""
        text.forEachIndexed { index, char ->
            scope.launch {
                delay(550L) // Adjust the delay to control the typing speed
                displayedText += char
            }
        }
    }

    Text(
        text = displayedText,
        fontFamily = getFont(name = "Poppins"),
        color = Color.White,
        softWrap = true,
        textAlign = TextAlign.Center
    )
}