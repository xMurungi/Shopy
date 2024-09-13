package com.ag_apps.core.data.auth

import android.content.SharedPreferences
import com.ag_apps.core.domain.SessionStorage
import com.ag_apps.core.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @author Ahmed Guedmioui
 */
class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences
) : SessionStorage {

    override suspend fun get(): User? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(AUTH_KEY, null)
            json?.let {
                Json.decodeFromString<UserSerializable>(it).toUser()
            }
        }
    }

    override suspend fun set(user: User?) {
        withContext(Dispatchers.IO) {
            if (user == null) {
                sharedPreferences.edit().remove(AUTH_KEY).apply()
                return@withContext
            }

            val json = Json.encodeToString(user.toUserSerializable())
            sharedPreferences.edit().putString(AUTH_KEY, json).commit()
        }
    }

    companion object {
        private const val AUTH_KEY = "AUTH_KEY"
    }

}













