package com.ag_apps.core.domain.models


/**
 * @author Ahmed Guedmioui
 */
data class User(
    val email: String,
    val userId: String,
    val customerId: String,
    val name: String,
    val image: String,
    val address: Address?,
    val wishlist: List<Int>,
    val cart: Map<Int, String?>,
    val orders: List<Order>
)

val EmptyFieldsUser = User(
    email = "",
    userId = "",
    customerId = "",
    name = "",
    image = "",
    address = EmptyFieldsAddress,
    wishlist = emptyList(),
    cart = mapOf(),
    orders = emptyList()
)
