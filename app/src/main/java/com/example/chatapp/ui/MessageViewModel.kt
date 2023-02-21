package com.example.chatapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.MessageApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    val messageApi: MessageApi
):ViewModel() {

    fun sendMessage(to:String,message:String){
        viewModelScope.launch {
            messageApi.sendMessage(to, message)
        }
    }
}