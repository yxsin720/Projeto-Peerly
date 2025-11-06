package com.example.Peerly.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "peerly_prefs")

suspend fun saveTutorPhoto(ctx: Context, tutorId: String, url: String?) {
    val key = stringPreferencesKey("tutor_photo_$tutorId")
    ctx.dataStore.edit { it[key] = url as String }
}

suspend fun readTutorPhoto(ctx: Context, tutorId: String): String? {
    val key = stringPreferencesKey("tutor_photo_$tutorId")
    return ctx.dataStore.data.map { it[key] }.first()
}
