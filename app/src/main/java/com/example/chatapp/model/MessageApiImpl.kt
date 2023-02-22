package com.example.chatapp.model

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    @OptIn(ExperimentalFoundationApi::class)
    override  fun getMessage(): Flow<List<Message>> =callbackFlow {
        try {
            val messageList = mutableListOf<Message>()
            val msgListener = (object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    snapshot.children.forEach { message ->
                        val from = message.child("email").getValue(String::class.java).toString()
                        val to = message.child("to").getValue(String::class.java).toString()
                        val message =
                            message.child("message").getValue(String::class.java).toString()
                        messageList.add(Message(from, to, message))
                    }
                    this@callbackFlow.trySendBlocking(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException().printStackTrace()
                    this@callbackFlow.trySendBlocking(messageList)
                }

            })
            database.getReference("messages").addValueEventListener(msgListener)
            this@callbackFlow.trySendBlocking(messageList)
            awaitClose {
                database.getReference("messages").removeEventListener(msgListener)
                channel.close()
                cancel()
            }
        }catch (e:Exception){
            this@callbackFlow.trySendBlocking(emptyList())
        }
    }

}