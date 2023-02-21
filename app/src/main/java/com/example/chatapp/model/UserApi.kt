package com.example.chatapp.model

interface UserApi {
    suspend fun login(email:String,password:String):Boolean
    suspend fun signUp(email:String,password:String):Boolean
}