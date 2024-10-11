package com.ag_apps.core.domain


/**
 * @author Ahmed Guedmioui
 */
data class User(
    val email: String,
    val id: String,
    val name: String,
    val card: Card?,
    val address: Address?,
)

val EmptyFieldsUser = User(
    email = "",
    id = "",
    name = "",
    card = EmptyFieldsCard,
    address = EmptyFieldsAddress
)
