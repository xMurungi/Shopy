package com.ag_apps.core.domain.abstractions

/**
 * @author Ahmed Guedmioui
 */
interface LocalStorageDataSource {
    suspend fun set(key: String, value: String)
    suspend fun get(key: String): String?
}