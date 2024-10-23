package com.ag_apps.core.domain


/**
 * @author Ahmed Guedmioui
 */
data class User(
    val email: String,
    val userId: String,
    val name: String,
    val image: String,
    val address: Address?,
    val wishlist: List<Int>,
    val cart: List<Int>,
)

val EmptyFieldsUser = User(
    email = "",
    userId = "",
    name = "",
    image = "",
    address = EmptyFieldsAddress,
    wishlist = emptyList(),
    cart = emptyList()
)
