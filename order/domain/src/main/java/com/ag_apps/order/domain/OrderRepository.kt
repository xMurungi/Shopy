package com.ag_apps.order.domain

import com.ag_apps.core.domain.Order
import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface OrderRepository {

    suspend fun getOrder(orderIndex: Int): Result<Order, DataError.Network>

}