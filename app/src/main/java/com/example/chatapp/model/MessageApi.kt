package com.example.chatapp.model

import kotlinx.coroutines.flow.Flow

interface MessageApi {
    suspend fun sendMessage(to:String,message:String)

     fun getMessage(): Flow<List<Message>>
}