package com.ghanshyam.geminichat

import android.graphics.Bitmap

sealed class UiEvents {
    data class UpdatePrompt(val newPrompt: String) : UiEvents()
    data class SendPrompt(
        val prompt: String,
        val bitmap: Bitmap?
    ) : UiEvents()
}