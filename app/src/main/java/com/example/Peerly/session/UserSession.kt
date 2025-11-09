package com.example.Peerly.session

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.Peerly.data.model.User

object UserSession {

    var currentUser by mutableStateOf<User?>(null)
        private set

    fun setUser(user: User) {
        currentUser = user
    }

    fun clear(ctx: Context? = null) {
        currentUser = null
        ctx?.let { UserPrefs.clearAll(it) }
    }

    fun updateAvatar(ctx: Context, url: String) {
        currentUser = currentUser?.copy(avatarUrl = url)
        UserPrefs.saveAvatar(ctx, url)
    }

    fun updateArea(ctx: Context, area: String) {
        currentUser = currentUser?.copy(area = area)
        UserPrefs.saveArea(ctx, area)
    }

    fun hydrateFromPrefs(ctx: Context) {
        val u = currentUser ?: return
        val area = UserPrefs.readArea(ctx)
        val avatar = UserPrefs.readAvatar(ctx)
        currentUser = u.copy(
            area = area ?: u.area,
            avatarUrl = avatar ?: u.avatarUrl
        )
    }

    val displayName: String
        get() = currentUser?.fullName
            ?: currentUser?.email?.substringBefore('@')
            ?: "Utilizador"
}
