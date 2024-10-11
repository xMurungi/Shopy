package com.ag_apps.core.domain


/**
 * @author Ahmed Guedmioui
 */
data class User(
    val email: String,
    val id: String,
    val name: String
)

val EmptyFieldsUser = User(
    email = "",
    id = "",
    name = ""
)
