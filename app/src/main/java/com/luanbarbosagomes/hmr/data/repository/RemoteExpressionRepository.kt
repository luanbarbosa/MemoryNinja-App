package com.luanbarbosagomes.hmr.data.repository

import com.google.firebase.database.DatabaseReference
import com.luanbarbosagomes.hmr.App.Companion.currentFirebaseUser
import com.luanbarbosagomes.hmr.App.Companion.firebaseDb
import com.luanbarbosagomes.hmr.UserNotFoundException
import com.luanbarbosagomes.hmr.data.Expression
import javax.inject.Inject

/**
 * Expression repository responsible for CRUD expressions remotely using Firebase Cloud Firestore.
 */
class RemoteExpressionRepository @Inject constructor() : IExpressionRepository {

    private val dbReference: DatabaseReference
        get() {
            val userUid = currentFirebaseUser?.uid ?: throw UserNotFoundException()
            return firebaseDb.getReference("users/$userUid/expressions")
        }

    override suspend fun save(expression: Expression) {
        val result = dbReference.push().setValue(expression)
        if (!result.isSuccessful && result.exception != null) {
            throw UnableToSaveError(result.exception?.localizedMessage ?: "")
        }
    }

    override suspend fun getAll(): List<Expression> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override suspend fun getRandom(): Expression? {
        TODO("Not yet implemented")
    }

    override suspend fun get(uid: Long): Expression? {
        TODO("Not yet implemented")
    }

}

class UnableToSaveError(message: String) : Throwable(message)
