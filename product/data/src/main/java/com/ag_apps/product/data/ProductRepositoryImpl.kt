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
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

        return productDataSource.getProducts(
            offset = offset,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }

    override suspend fun addProductToWishlist(
        productId: String
    ): Flow<Result<String, DataError.Network>> {
        return userDataSource.addProductToWishlist(productId)
    }

    override suspend fun addProductToCart(
        productId: String
    ): Flow<Result<String, DataError.Network>> {
        return userDataSource.addProductToCart(productId)
    }
}