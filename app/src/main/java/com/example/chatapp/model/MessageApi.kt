package com.example.chatapp.model

interface MessageApi {
    suspend fun sendMessage(to:String,message:String)

    suspend fun getMessage():List<Message>
}