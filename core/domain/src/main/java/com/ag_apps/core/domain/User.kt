package com.ag_apps.core.domain


/**
 * @author Ahmed Guedmioui
 */
data class User(
    val email: String,
    val id: String,
    val name: String,
    val image: String,
    val card: Card?,
    val address: Address?,
)

val EmptyFieldsUser = User(
    email = "",
    id = "",
    name = "",
    image = "",
    card = EmptyFieldsCard,
    address = EmptyFieldsAddress
)
