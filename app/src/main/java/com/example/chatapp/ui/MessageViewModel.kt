package com.example.chatapp.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Message
import com.example.chatapp.model.MessageApi
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
   private val auth:FirebaseAuth,
    val messageApi: MessageApi
):ViewModel() {

    var messages:MutableList<Message> by mutableStateOf(mutableListOf<Message>())


    fun checkMe(email:String):Boolean {
        return email == auth.currentUser?.email
    }

    fun GetEmail(): String {
        return auth.currentUser?.email.toString()
    }


    init{
        getMessages()
    }
    fun sendMessage(to:String,message:String){
        viewModelScope.launch {
            messageApi.sendMessage(to, message)
        }
    }

     fun getMessages(){
        messageApi.getMessage().onEach {
            messages = it.toMutableList()

        }.launchIn(viewModelScope)
    }
}