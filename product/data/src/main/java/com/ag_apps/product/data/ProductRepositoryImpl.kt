package com.ag_apps.product.data

import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.product.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Ahmed Guedmioui
 */
class ProductRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource
) : ProductRepository {

    override suspend fun getProducts(
        refresh: Boolean, minPrice: Int?, maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {
        return productDataSource.getProducts(
            refresh = refresh,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }

    override suspend fun addProductToWishlist(
        productId: String
    ): Flow<Result<String, DataError.Network>> {
        return flow {
            userDataSource.getUser().collect { userResult ->
                when (userResult) {
                    is Result.Error -> {
                        emit(Result.Error(userResult.error))
                    }

                    is Result.Success -> {

                        val wishlist = userResult.data.wishlist.toMutableList()
                        wishlist.add(0, productId)
                        val user = userResult.data.copy(
                            wishlist = wishlist
                        )
                        userDataSource.updateUser(user).collect {
                            emit(it)
                        }

                    }
                }
            }
        }
    }

    override suspend fun addProductToCart(
        productId: String
    ): Flow<Result<String, DataError.Network>> {
        return flow {
            userDataSource.getUser().collect { userResult ->
                when (userResult) {
                    is Result.Error -> {
                        emit(Result.Error(userResult.error))
                    }

                    is Result.Success -> {

                        val cart = userResult.data.cart.toMutableList()
                        cart.add(0, productId)
                        val user = userResult.data.copy(
                            cart = cart
                        )
                        userDataSource.updateUser(user).collect {
                            emit(it)
                        }

                    }
                }
            }
        }
    }
}