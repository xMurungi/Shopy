package com.ag_apps.core.presentation.model

import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */

fun Product.toProductUI() = ProductUI(
    productId = productId,
    title = title,
    description = description,
    image = if (images.isNotEmpty()) images.random() else "",
    price = price,
    categoryName = categoryName,
    isInWishList = isInWishList,
    isInCartList = isInCartList
)