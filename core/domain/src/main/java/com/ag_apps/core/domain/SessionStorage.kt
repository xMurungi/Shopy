package com.ag_apps.core.domain

/**
 * @author Ahmed Guedmioui
 */
interface SessionStorage {
    suspend fun get(): User?
    suspend fun set(user: User?)
}