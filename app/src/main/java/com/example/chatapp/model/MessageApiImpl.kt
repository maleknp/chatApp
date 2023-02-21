package com.example.chatapp.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class MessageApiImpl @Inject constructor(
    val auth:FirebaseAuth,
    val database:FirebaseDatabase
):MessageApi {
    override suspend fun sendMessage(to:String,message:String) {
        if(message.isNotBlank()){
            Log.i("XtoX",message.toString())
            val messageRef = database.getReference("messages").push()
            messageRef.setValue(
                Message(auth.currentUser?.email ?: "", to, message)
            )
        }
    }

    override suspend fun getMessage(): List<Message> {
        TODO("Not yet implemented")
    }

}