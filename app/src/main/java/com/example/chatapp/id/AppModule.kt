package com.example.chatapp.id

import com.example.chatapp.model.MessageApi
import com.example.chatapp.model.MessageApiImpl
import com.example.chatapp.model.UserApi
import com.example.chatapp.model.UserApiImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun makeAuth():FirebaseAuth=FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun makeUserApi(auth:FirebaseAuth):UserApi = UserApiImpl(auth)



    @Provides
    @Singleton
    fun makeDatabase():FirebaseDatabase=FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun makeMessageApi(auth:FirebaseAuth,db:FirebaseDatabase):MessageApi = MessageApiImpl(auth,db)
}