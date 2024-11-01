package com.ag_apps.core.data

import android.content.SharedPreferences
import com.ag_apps.core.domain.abstractions.LocalStorageDataSource

/**
 * @author Ahmed Guedmioui
 */
class PrefsLocalStorageDataSource(
    private val sharedPreferences: SharedPreferences
): LocalStorageDataSource {

    override suspend fun set(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override suspend fun get(key: String): String? {
       return sharedPreferences.getString(key, null)
    }
}