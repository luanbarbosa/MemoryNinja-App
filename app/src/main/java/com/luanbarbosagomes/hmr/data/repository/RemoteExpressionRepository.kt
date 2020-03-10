package com.luanbarbosagomes.hmr.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.luanbarbosagomes.hmr.App.Companion.currentFirebaseUser
import com.luanbarbosagomes.hmr.App.Companion.firebaseDb
import com.luanbarbosagomes.hmr.UserNotFoundException
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.ExpressionLean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    override suspend fun getAll(): List<Expression> =
        withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                dbReference.addListenerForSingleValueEvent(
                    FirebaseValueEventListener(
                        onDataChange = { continuation.resume(it) },
                        onError = { continuation.resumeWithException(it.toException()) }
                    )
                )
            }
        }.toExpressionList()

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

private fun DataSnapshot.toExpressionList(): List<Expression> =
    this.children
        .map { it.getValue(ExpressionLean::class.java) }
        .mapNotNull { it?.toExpression() }

/**
 * Used to "convert" Firebase Database's fetch listener for a coroutine type handling.
 * Not my code! https://gist.github.com/Sloy/f68921a2ead6466e530ff4b18c180c4d
 */
class FirebaseValueEventListener(
    val onDataChange: (DataSnapshot) -> Unit,
    val onError: (DatabaseError) -> Unit
) : ValueEventListener {
    override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
    override fun onCancelled(error: DatabaseError) = onError.invoke(error)
}

class UnableToSaveError(message: String) : Throwable(message)
