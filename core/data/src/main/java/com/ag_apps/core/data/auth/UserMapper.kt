package com.ag_apps.core.data.auth

import com.ag_apps.core.domain.User


/**
 * @author Ahmed Guedmioui
 */

fun User.toUserSerializable(): UserSerializable {
    return UserSerializable(
        name = name,
        id = id,
        profilePicture = profilePicture
    )
}
fun UserSerializable.toUser(): User {
    return User(
        name = name,
        id = id,
        profilePicture = profilePicture
    )
}