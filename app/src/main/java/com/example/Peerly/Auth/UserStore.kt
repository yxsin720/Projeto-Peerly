package com.example.Peerly.Auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userStore by preferencesDataStore("user_store")

data class LoggedUser(
    val id: String,
    val email: String,
    val fullName: String,
    val role: String,
    val avatarUrl: String?,
    val language: String
)

class UserStore(private val context: Context) {
    private companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL   = stringPreferencesKey("email")
        val NAME    = stringPreferencesKey("full_name")
        val ROLE    = stringPreferencesKey("role")
        val AVATAR  = stringPreferencesKey("avatar_url")
        val LANG    = stringPreferencesKey("language")
    }

    suspend fun saveAll(user: LoggedUser) {
        context.userStore.edit {
            it[USER_ID] = user.id
            it[EMAIL]   = user.email
            it[NAME]    = user.fullName
            it[ROLE]    = user.role
            it[AVATAR]  = user.avatarUrl ?: ""
            it[LANG]    = user.language
        }
    }

    suspend fun setAvatar(url: String) {
        context.userStore.edit { it[AVATAR] = url }
    }

    val current: Flow<LoggedUser?> = context.userStore.data.map { p ->
        val id = p[USER_ID] ?: return@map null
        LoggedUser(
            id        = id,
            email     = p[EMAIL].orEmpty(),
            fullName  = p[NAME].orEmpty(),
            role      = p[ROLE].orEmpty(),
            avatarUrl = p[AVATAR],
            language  = p[LANG] ?: "pt"
        )
    }

    suspend fun clear() { context.userStore.edit { it.clear() } }
}
