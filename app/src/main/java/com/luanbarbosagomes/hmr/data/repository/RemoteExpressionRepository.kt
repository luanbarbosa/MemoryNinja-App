package com.luanbarbosagomes.hmr.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.luanbarbosagomes.hmr.App.Companion.currentFirebaseUser
import com.luanbarbosagomes.hmr.App.Companion.firebaseDb
import com.luanbarbosagomes.hmr.UserNotFoundException
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.ExpressionLean
import com.luanbarbosagomes.hmr.utils.ignoreError
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

/**
 * Expression repository responsible for CRUD expressions remotely using Firebase Cloud Firestore.
 */
class RemoteExpressionRepository @Inject constructor() : BaseExpressionRepository() {

    private val usersDocument: DocumentReference
        get() = firebaseDb
            .collection("users")
            .document(currentFirebaseUser?.uid ?: throw UserNotFoundException())

    private val expressionsCollection: CollectionReference =
        usersDocument.collection("expressions")

    override suspend fun save(expression: Expression) {
        expressionsCollection.add(expression).await()
    }

    override suspend fun getAll(): List<Expression> =
        expressionsCollection
            .get()
            .await()
            .documents
            .mapNotNull {
                ignoreError {
                    it.toObject(ExpressionLean::class.java)?.toExpression()
                }
            }

    override suspend fun deleteAll() {
        val expressions = getAll()
        firebaseDb.runBatch {
            expressions.forEach { expression ->
                expressionsCollection.document(expression.uid).delete()
            }
        }
    }

    override suspend fun getRandom(): Expression? =
        with(getAll()) {
            this[Random.nextInt(0, this.size - 1)]
        }

    override suspend fun get(uid: String): Expression? =
        expressionsCollection
            .whereEqualTo("uid", uid)
            .get()
            .await()
            .documents
            .first()
            .let {
                it.toObject(ExpressionLean::class.java)?.toExpression()
            }

}
