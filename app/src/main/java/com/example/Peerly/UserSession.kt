package com.example.Peerly.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.Peerly.data.model.User


object UserSession {

    var currentUser: User? by mutableStateOf(null)
        private set


    fun setUser(user: User) {
        currentUser = user
    }


    fun clear() {
        currentUser = null
    }


    fun updateAvatar(url: String) {
        currentUser = currentUser?.copy(avatarUrl = url)
    }


    val displayName: String
        get() = currentUser?.fullName
            ?: currentUser?.email?.substringBefore('@')
            ?: "Utilizador"
}
