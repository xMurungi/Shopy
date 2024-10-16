package com.ag_apps.core.product_data.dto

import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */

fun ProductDto.toProduct(): Product = Product(
    category = category.toCategory(),
    description = description,
    id = id,
    images = images,
    price = price,
    title = title,
    isInWishList = false,
    isInCartList = false,
)

fun CategoryDto.toCategory(): Category = Category(
    id = id,
    image = image,
    name = name
)