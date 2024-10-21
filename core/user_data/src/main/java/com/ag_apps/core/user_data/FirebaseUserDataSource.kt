package com.ag_apps.core.user_data

import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.domain.util.map
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class FirebaseUserDataSource(
    private val firestoreClient: FirestoreClient
) : UserDataSource {

    private val tag = "UserDataSource"

    private var firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun insertUser(user: User): Result<String, DataError.Network> {
        return firestoreClient.insertUser(user).first()
    }

    override suspend fun updateUser(user: User): Result<String, DataError.Network> {
        return firestoreClient.updateUser(user).first()
    }

    override suspend fun getUser(): Result<User, DataError.Network> {
        val userEmail = firebaseAuth.currentUser?.email

        if (userEmail == null) {
            return Result.Error(DataError.Network.NOT_FOUND)
        }

        return firestoreClient.getUser(userEmail).first()

    }

    override suspend fun addProductToWishlist(
        productId: String
    ): Result<Unit, DataError.Network> {

        val userResult = getUser()
        if (userResult is Result.Error) {
            return Result.Error(userResult.error)
        }

        if (userResult is Result.Success) {
            val wishlist = userResult.data.wishlist.toMutableList()
            wishlist.add(0, productId)
            val user = userResult.data.copy(
                wishlist = wishlist
            )

            val updateResult = updateUser(user)
            if (updateResult is Result.Error) {
                return Result.Error(updateResult.error)
            }
        }

        return Result.Success(Unit)
    }

    override suspend fun removeProductToWishlist(
        productId: String
    ): Result<Unit, DataError.Network> {

        val userResult = getUser()
        if (userResult is Result.Error) {
            return Result.Error(userResult.error)
        }

        if (userResult is Result.Success) {
            val wishlist = userResult.data.wishlist.toMutableList()
            wishlist.remove(productId)
            val user = userResult.data.copy(
                wishlist = wishlist
            )

            val updateResult = updateUser(user)
            if (updateResult is Result.Error) {
                return Result.Error(updateResult.error)
            }
        }

        return Result.Success(Unit)
    }

    override suspend fun addProductToCart(
        productId: String
    ): Result<Unit, DataError.Network> {

        val userResult = getUser()
        if (userResult is Result.Error) {
            return Result.Error(userResult.error)
        }

        if (userResult is Result.Success) {
            val cart = userResult.data.cart.toMutableList()
            cart.add(0, productId)
            val user = userResult.data.copy(
                cart = cart
            )

            val updateResult = updateUser(user)
            if (updateResult is Result.Error) {
                return Result.Error(updateResult.error)
            }
        }

        return Result.Success(Unit)
    }

    override suspend fun removeProductToCart(
        productId: String
    ): Result<Unit, DataError.Network> {

        val userResult = getUser()
        if (userResult is Result.Error) {
            return Result.Error(userResult.error)
        }

        if (userResult is Result.Success) {
            val cart = userResult.data.cart.toMutableList()
            cart.remove(productId)
            val user = userResult.data.copy(
                cart = cart
            )

            val updateResult = updateUser(user)
            if (updateResult is Result.Error) {
                return Result.Error(updateResult.error)
            }
        }

        return Result.Success(Unit)
    }

    override fun isLoggedIn(): Boolean {
        if (firebaseAuth.currentUser != null) {
            Timber.tag(tag).d("Already logged in")
            return true
        }

        Timber.tag(tag).d("Not already logged in")
        return false
    }

    override fun logout() {
        Timber.tag(tag).d("logout")
        firebaseAuth.signOut()
    }
}
