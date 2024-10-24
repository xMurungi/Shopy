package com.ag_apps.core.product_data.dto

import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product
import kotlin.random.Random

/**
 * @author Ahmed Guedmioui
 */

fun ProductDto.toProduct(): Product = Product(
    title = title,
    categoryName = category.name,
    description = description,
    brand = if (category.name.contains("clothe")) "H&M" else "Brand",
    productId = id,
    images = images,
    thumbnail = images.random(),
    rating = Random.nextInt(5, 10).toFloat(),
    price = price,
    discount = Random.nextInt(0, 30),
    filter = if (category.name.contains("clothe")) "Size" else "Color",
    filterList = if (category.name.contains("clothe")) {
        listOf("S", "M", "L", "XL")
    } else {
        listOf("Black", "White", "Gray")
    },
    isInWishList = false,
    isInCartList = false,
)

fun CategoryDto.toCategory(): Category = Category(
    id = id,
    image = image,
    name = name
)