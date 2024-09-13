package com.ag_apps.core.network

import android.util.Log
import com.ag_apps.core.domain.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


/**
 * @author Ahmed Guedmioui
 */
class FirestoreClient {

    private val TAG = "FirestoreClient"

    private var db = FirebaseFirestore.getInstance()

    fun upsertUser(user: User) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Timber.tag(TAG).d("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Timber.tag(TAG).d("Error adding document: ${e.message}")
            }
    }



}
