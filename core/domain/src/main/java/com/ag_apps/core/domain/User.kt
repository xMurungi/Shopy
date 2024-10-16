package com.ag_apps.core.domain


/**
 * @author Ahmed Guedmioui
 */
data class User(
    val email: String,
    val id: String,
    val name: String,
    val image: String,
    val address: Address?,
    val wishlist: List<String>,
    val cart: List<String>,
)

val EmptyFieldsUser = User(
    email = "",
    id = "",
    name = "",
    image = "",
    address = EmptyFieldsAddress,
    wishlist = emptyList(),
    cart = emptyList()
)
