package com.ag_apps.core.data.auth

import kotlinx.serialization.Serializable


/**
 * @author Ahmed Guedmioui
 */
@Serializable
data class UserSerializable(
    val name: String,
    val id: String,
    val profilePicture: String
)
