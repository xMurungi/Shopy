package com.ag_apps.order.data

import com.ag_apps.core.domain.Order
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.order.domain.OrderRepository

/**
 * @author Ahmed Guedmioui
 */
class OrderRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val productDataSource: ProductDataSource,
) : OrderRepository {

    override suspend fun getOrders(): Result<List<Order>, DataError.Network> {
        return when (val userResult = userDataSource.getUser()) {
            is Result.Error -> {
                Result.Error(userResult.error)
            }

            is Result.Success -> {
                Result.Success(userResult.data.orders.reversed())
            }
        }
    }

    override suspend fun getOrder(
        orderId: Int
    ): Result<Order, DataError.Network> {
        return when (val userResult = userDataSource.getUser()) {
            is Result.Error -> {
                Result.Error(userResult.error)
            }

            is Result.Success -> {
                val order = userResult.data.orders.find { it.orderId == orderId }
                if (order != null) {
                    Result.Success(order)
                } else {
                    Result.Error(DataError.Network.NOT_FOUND)
                }
            }
        }
    }

    override suspend fun getOrderProducts(
        productsMap: Map<Int, String?>
    ): Result<List<Product>, DataError.Network> {
        val products = mutableListOf<Product>()
        productsMap.forEach { productMap ->
            when (val productResult = productDataSource.getProduct(productMap.key)) {
                is Result.Error -> Unit
                is Result.Success -> {
                    products.add(productResult.data.copy(selectedFilter = productMap.value))
                }
            }
        }

        return Result.Success(products)
    }

    override suspend fun addProductToWishlist(
        productId: Int
    ): Result<Unit, DataError.Network> {
        return userDataSource.addProductToWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(
        productId: Int
    ): Result<Unit, DataError.Network> {
        return userDataSource.removeProductFromWishlist(productId)
    }

    override suspend fun addProductToCart(
        productId: Int, filter: String?
    ): Result<Unit, DataError.Network> {
        return userDataSource.addProductToCart(productId, filter)
    }

    override suspend fun removeProductFromCart(
        productId: Int
    ): Result<Unit, DataError.Network> {
        return userDataSource.removeProductFromCart(productId)
    }

}