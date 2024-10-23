package com.ag_apps.core.presentation.model

import com.ag_apps.core.domain.Product
import kotlin.random.Random

/**
 * @author Ahmed Guedmioui
 */

fun Product.toProductUI(): ProductUI {
    return ProductUI(
        productId = productId,
        title = title,
        description = description,
        image = this.images.random(),
        price = price,
        rating = Random.nextInt(from = 6, until = 10),
        categoryName = categoryName,
        isInWishList = isInWishList,
        isInCartList = isInCartList
    )
}