package com.ag_apps.core.presentation.util

import com.ag_apps.core.domain.models.Product

/**
 * @author Ahmed Guedmioui
 */


val PreviewProducts = listOf(
    Product(
        productId = 1,
        title = "Blue Shirt with no logo and simple design",
        description = "Blue Shirt with no logo and simple design that is very comfortable and stylish for summer time and parties.",
        brand = "Zara",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 59.99,
        selectedFilter = "XL",
        discount = 0,
        categoryId = 1,
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        categoryName = "Clothes",
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 1",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35,
        discount = 20,
        categoryId = 1,
        filter = "Size",
        categoryName = "Clothes",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 3,
        title = "Product 1",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        categoryName = "Clothes",
        price = 100.35,
        discount = 20,
        categoryId = 1,
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 4,
        title = "Product 1",
        categoryName = "Clothes",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35,
        discount = 20,
        categoryId = 1,
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 5,
        title = "Product 1",
        categoryName = "Clothes",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35,
        discount = 20,
        categoryId = 1,
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    )
)