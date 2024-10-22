package com.ag_apps.core.presentation.model

import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */

fun Product.toProductUI() : ProductUI {
    var productImage = ""
    if (this.images.isNotEmpty()) {
        for (image in this.images) {
            println("toProductUI + $image")
            if (
                image.contains("png") ||
                image.contains("jpeg") ||
                image.contains("jpg")
            ) {
                productImage = image
                break
            }
        }
    }

   return ProductUI(
        productId = productId,
        title = title,
        description = description,
        image = productImage,
        price = price,
        categoryName = categoryName,
        isInWishList = isInWishList,
        isInCartList = isInCartList
    )
}