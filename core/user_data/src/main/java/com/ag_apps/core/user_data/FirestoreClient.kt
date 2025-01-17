package com.ag_apps.core.user_data

import com.ag_apps.core.domain.models.Address
import com.ag_apps.core.domain.models.Order
import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class FirestoreClient(
    private val applicationScope: CoroutineScope,
) {

    private val tag = "FirestoreClient"

    private var firestore = FirebaseFirestore.getInstance()

    fun insertUser(user: User): Flow<Result<String, DataError.Network>> {
        return callbackFlow {
            firestore.collection("users")
                .add(user.toHashMap())
                .addOnSuccessListener { document ->
                    Timber.tag(tag).d("insertUser with ID: ${document.id}")
                    applicationScope.launch {
                        // Update the user in firestore with the id field
                        // because when we insert a new user, the id field is empty
                        updateUser(user.copy(userId = document.id)).collect { updateResult ->
                            when (updateResult) {
                                is Result.Success -> trySend(Result.Success(document.id))
                                is Result.Error -> trySend(Result.Error(updateResult.error))
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Timber.tag(tag).d("Error inserting user: ${e.message}")
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                }

            awaitClose { close() }
        }
    }

    fun updateUser(user: User): Flow<Result<String, DataError.Network>> {
        return callbackFlow {
            firestore.collection("users")
                .document(user.userId)
                .set(user.toHashMap())
                .addOnSuccessListener {
                    Timber.tag(tag).d("updateUser with ID: ${user.userId}")
                    trySend(Result.Success(user.userId))
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Timber.tag(tag).d("Error updating user: ${e.message}")
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                }

            awaitClose { close() }
        }
    }

    fun getUser(email: String): Flow<Result<User, DataError.Network>> {
        return callbackFlow {
            firestore.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    var user: User? = null

                    for (document in result) {
                        if (document.data["email"] == email) {
                            user = document.data.toUser()
                            Timber.tag(tag).d("user found: ${user.email}")
                            trySend(Result.Success(user))
                        }
                    }

                    if (user == null) {
                        Timber.tag(tag).d("user not found: $email")
                        trySend(Result.Error(DataError.Network.NOT_FOUND))
                    }
                }
                .addOnFailureListener { e ->
                    Timber.tag(tag).d("Error getting users collection null")
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                }

            awaitClose { close() }
        }
    }

    private fun User.toHashMap(): HashMap<String, Any> {
        val address = mapOf(
            "street" to (address?.street ?: ""),
            "city" to (address?.city ?: ""),
            "region" to (address?.region ?: ""),
            "zipCode" to (address?.zipCode ?: ""),
            "country" to (address?.country ?: "")
        )

        val orders = orders.map { order ->
            mapOf(
                "orderId" to order.orderId.toLong(),
                "date" to order.date,
                "totalPrice" to order.totalPrice,
                "address" to mapOf(
                    "street" to order.address?.street,
                    "city" to order.address?.city,
                    "region" to order.address?.region,
                    "zipCode" to order.address?.zipCode,
                    "country" to order.address?.country
                ),
                "products" to order.products.mapKeys { it.key.toString() },
            )
        }

        val userMap = hashMapOf(
            "email" to email,
            "userId" to userId,
            "customerId" to customerId,
            "name" to name,
            "image" to image,
            "address" to address,
            "wishlist" to wishlist.map { it.toString() },
            "cart" to cart.mapKeys { it.key.toString() },
            "orders" to orders
        )

        return userMap
    }

    private fun Map<String, Any>.toUser(): User {
        val address = (this["address"] as? Map<*, *>)?.let {
            Address(
                street = it["street"] as String,
                city = it["city"] as String,
                region = it["region"] as String,
                zipCode = it["zipCode"] as String,
                country = it["country"] as String
            )
        }

        val wishlist = (this["wishlist"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()

        val cart = (this["cart"] as? Map<*, *>)?.mapNotNull { (key, value) ->
            (key as? String)?.let { intKey ->
                intKey.toInt() to (value as? String)
            }
        }?.toMap() ?: emptyMap()

        val orders = (this["orders"] as? List<*>)?.mapNotNull { orderMap ->
            (orderMap as? Map<*, *>)?.let {
                Order(
                    orderId = (it["orderId"] as Long).toInt() ,
                    date = it["date"] as Long,
                    totalPrice = it["totalPrice"] as Double,
                    address = (it["address"] as? Map<*, *>)?.let { addr ->
                        Address(
                            street = addr["street"] as String,
                            city = addr["city"] as String,
                            region = addr["region"] as String,
                            zipCode = addr["zipCode"] as String,
                            country = addr["country"] as String
                        )
                    },
                    products = (it["products"] as? Map<*, *>)?.mapNotNull { (key, value) ->
                        (key as? String)?.let { intKey ->
                            intKey.toInt() to (value as? String)
                        }
                    }?.toMap() ?: emptyMap()
                )
            }
        } ?: emptyList()

        return User(
            email = this["email"] as String,
            userId = this["userId"] as String,
            customerId = this["customerId"] as String,
            name = this["name"] as String,
            image = this["image"] as String,
            address = address,
            wishlist = wishlist.map { it.toInt() },
            cart = cart,
            orders = orders
        )
    }
}
