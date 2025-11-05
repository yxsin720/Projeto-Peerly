package com.example.Peerly.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.Peerly.data.model.User

/**
 * Sessão simples em memória para guardar o utilizador autenticado.
 * (Se quiseres persistir entre execuções, usa DataStore.)
 */
object UserSession {

    /** Utilizador autenticado (stateful para recomposições em Compose). */
    var currentUser: User? by mutableStateOf(null)
        private set

    /** Define o utilizador autenticado. */
    fun setUser(user: User) {
        currentUser = user
    }

    /** Limpa a sessão (logout). */
    fun clear() {
        currentUser = null
    }

    /** Atualiza apenas o avatar do utilizador atual. */
    fun updateAvatar(url: String) {
        currentUser = currentUser?.copy(avatarUrl = url)
    }

    /** Nome amigável para UI. */
    val displayName: String
        get() = currentUser?.fullName
            ?: currentUser?.email?.substringBefore('@')
            ?: "Utilizador"
}
