package com.example.chatapp.model

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserApiImpl @Inject constructor(
   val auth: FirebaseAuth
):UserApi {
    override suspend fun login(email:String,password:String): Boolean {
        return auth.signInWithEmailAndPassword(email,password).await() != null
    }

    override suspend fun signUp(email:String,password:String): Boolean {
        return auth.createUserWithEmailAndPassword(email,password).await() != null
    }


}