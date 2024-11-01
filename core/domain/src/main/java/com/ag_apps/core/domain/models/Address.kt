package com.ag_apps.core.domain.models


/**
 * @author Ahmed Guedmioui
 */
data class Address(
    val street: String,
    val city: String,
    val region: String,
    val zipCode: String,
    val country: String
)

val EmptyFieldsAddress = Address(
    street = "",
    city = "",
    region = "",
    zipCode = "",
    country = ""
)
