package com.ag_apps.core.product_data.dto

import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import kotlin.random.Random

/**
 * @author Ahmed Guedmioui
 */

fun ProductDto.toProduct(): Product {

    val brand = if (category.name.contains("cloth", ignoreCase = true)) {
        "H&M"
    } else if (category.name.contains("electronic", ignoreCase = true)) {
        "Samsung"
    } else if (category.name.contains("shoe", ignoreCase = true)) {
        "Nike"
    } else {
        "C-Corp"
    }

    return Product(
        title = title,
        categoryName = category.name,
        description = description,
        brand = brand,
        productId = id,
        images = images,
        thumbnail = images.random(),
        rating = 7.5f,
        price = price.toDouble(),
        discount = 0,
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