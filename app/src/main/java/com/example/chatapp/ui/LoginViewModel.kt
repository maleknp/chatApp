package com.example.chatapp.ui

import android.icu.text.CaseMap.Title
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.model.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
val userApi: UserApi
): ViewModel() {



    fun logIn(email:String,password:String){
        viewModelScope.launch {
           userApi.login(email,password)
        }
    }
    fun signUp(email:String,password:String){
        viewModelScope.launch {
            userApi.signUp(email,password)
        }
    }


}