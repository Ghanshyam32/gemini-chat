package com.ghanshyam.geminichat

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Api {

    val key = "your-api-key"


    //for text input
    suspend fun getResponse(prompt: String): Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro", apiKey = key
        )

        try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }
            return Chat(
                prompt = response.text ?: "error",
                bitmap = null, isFromUser = false
            )
        } catch (e: Exception) {
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }

    }


    //for image input
    suspend fun getImageResponse(prompt: String, bitmap: Bitmap): Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision", apiKey = key
        )

        try {

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }
            return Chat(
                prompt = response.text ?: "error",
                bitmap = null, isFromUser = false
            )
        } catch (e: Exception) {
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }

    }
}