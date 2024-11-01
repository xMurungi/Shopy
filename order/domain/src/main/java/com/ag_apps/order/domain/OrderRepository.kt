package com.ag_apps.order.domain

import com.ag_apps.core.domain.Order
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface OrderRepository {

    suspend fun getOrders(): Result<List<Order>, DataError.Network>

    suspend fun getOrder(
        orderId: Int
    ): Result<Order, DataError.Network>

    suspend fun getOrderProducts(
        productsMap: Map<Int, String?>
    ): Result<List<Product>, DataError.Network>

    suspend fun addProductToWishlist(
        productId: Int
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromWishlist(
        productId: Int
    ): Result<Unit, DataError.Network>

    suspend fun addProductToCart(
        productId: Int, filter: String?
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromCart(
        productId: Int
    ): Result<Unit, DataError.Network>


}