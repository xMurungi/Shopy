package com.ag_apps.order.data

import com.ag_apps.core.domain.Order
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.order.domain.OrderRepository

/**
 * @author Ahmed Guedmioui
 */
class OrderRepositoryImpl(
    private val userDataSource: UserDataSource
) : OrderRepository {

    override suspend fun getOrder(
        orderIndex: Int
    ): Result<Order, DataError.Network> {
        return when(val userResult = userDataSource.getUser()) {
            is Result.Error -> {
                Result.Error(userResult.error)
            }

            is Result.Success -> {
                Result.Success(userResult.data.orders[orderIndex])
            }
        }
    }
}