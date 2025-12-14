package com.example.Peerly.session

import android.content.Context
import android.content.SharedPreferences

object UserPrefs {
    private const val FILE = "peerly_user_prefs"
    private const val KEY_AREA = "area"
    private const val KEY_AVATAR = "avatar"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun saveArea(ctx: Context, area: String) {
        prefs(ctx).edit().putString(KEY_AREA, area).apply()
    }

    fun readArea(ctx: Context): String? =
        prefs(ctx).getString(KEY_AREA, null)

    fun saveAvatar(ctx: Context, url: String) {
        prefs(ctx).edit().putString(KEY_AVATAR, url).apply()
    }

    fun readAvatar(ctx: Context): String? =
        prefs(ctx).getString(KEY_AVATAR, null)

    fun clearAll(ctx: Context) {
        prefs(ctx).edit().clear().apply()
    }
}

