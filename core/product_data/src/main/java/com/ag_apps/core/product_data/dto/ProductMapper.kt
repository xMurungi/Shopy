package com.ag_apps.core.product_data.dto

import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product
import kotlin.random.Random

/**
 * @author Ahmed Guedmioui
 */

fun ProductDto.toProduct(): Product {

    val discount = Random.nextInt(-20, 35)

    return Product(
        title = title,
        categoryName = category.name,
        description = description,
        brand = if (category.name.contains("clothe")) "H&M" else "Brand",
        productId = id,
        images = images,
        thumbnail = images.random(),
        rating = Random.nextInt(5, 10).toFloat(),
        price = price.toDouble(),
        discount = if (discount < 20) 0 else discount,
        filter = if (category.name.contains("clothe", ignoreCase = true)) "Size" else "Color",
        filterList = if (category.name.contains("clothe", ignoreCase = true)) {
            listOf("S", "M", "L", "XL")
        } else {
            listOf("Black", "White", "Gray")
        },
        isInWishList = false,
        isInCartList = false,
    )
}

fun CategoryDto.toCategory(): Category = Category(
    categoryId = id,
    image = image,
    name = name
)