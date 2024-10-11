package com.ag_apps.core.domain


/**
 * @author Ahmed Guedmioui
 */
data class Address(
    val address: String,
    val city: String,
    val region: String,
    val zipCode: String,
    val country: String
)

val EmptyFieldsAddress = Address(
    address = "",
    city = "",
    region = "",
    zipCode = "",
    country = ""
)
