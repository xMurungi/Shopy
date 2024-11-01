package com.ag_apps.core.domain.models


/**
 * @author Ahmed Guedmioui
 */
data class Card(
    val nameOnCard: String,
    val cardNumber: String,
    val expireDate: String,
    val cvv: String
)

val EmptyFieldsCard = Card(
    nameOnCard = "",
    cardNumber = "",
    expireDate = "",
    cvv = ""
)
