package com.ghanshyam.geminichat

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun onEvent(events: UiEvents) {
        when (events) {
            is UiEvents.SendPrompt -> {
                if (events.prompt.isNotEmpty()) {
//                    events.bitmap?.let { addPrompt(events.prompt, it) }
                    addPrompt(events.prompt, events.bitmap)
                    if (events.bitmap != null) {
                        getImageResponse(events.prompt, events.bitmap)
                    } else {
                        getResponse(events.prompt)

                    }

                }
            }

            is UiEvents.UpdatePrompt -> {
                _chatState.update {
                    it.copy(prompt = events.newPrompt)
                }
            }
        }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?) {
        if (prompt.isNotEmpty() || bitmap != null) {
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, Chat(prompt, bitmap, true))
                    },
                    prompt = "",
                    bitmap = null
                )
            }
        }
    }




    private fun getResponse(prompt: String) {
        viewModelScope.launch {
            val chat = Api.getResponse(prompt)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }

    private fun getImageResponse(prompt: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val chat = Api.getImageResponse(prompt, bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }

}