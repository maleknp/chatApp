package com.example.chatapp

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Message (
    val mess: String,
    val sen: Boolean
):Parcelable