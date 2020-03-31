package com.luanbarbosagomes.hmr.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.luanbarbosagomes.hmr.App.Companion.currentFirebaseUser
import com.luanbarbosagomes.hmr.App.Companion.firebaseDb
import com.luanbarbosagomes.hmr.UserNotFoundException
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.copy
import kotlinx.coroutines.tasks.await
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

    override suspend fun update(expression: Expression) {
        getRaw(expression.uid)?.let {
            expressionsCollection.document(it.id).set(expression, SetOptions.merge())
        }
    }

    override suspend fun updateLevel(uid: String, correctAnswer: Boolean) {
        val rawExpression = getRaw(uid) ?: return
        val expression = rawExpression.toObject(Expression::class.java)?.apply {
            if (correctAnswer) bumpKnowledgeLevel()
            else downgradeKnowledgeLevel()
        } ?: return

        expressionsCollection
            .document(rawExpression.id)
            .set(expression, SetOptions.merge())
    }

    override suspend fun getAll(): List<Expression> =
        expressionsCollection
            .get()
            .await()
            .documents
            .mapNotNull {
                it.toObject(Expression::class.java)
            }

    override suspend fun deleteAll() {
        val expressions = getAll()
        firebaseDb.runBatch {
            expressions.forEach { expression ->
                expressionsCollection.document(expression.uid).delete()
            }
        }
    }

    override suspend fun delete(expression: Expression) {
        getRaw(expression.uid)?.let {
            expressionsCollection
                .document(it.id)
                .delete()
                .await()
        }
    }

    override suspend fun getRandom(): Expression? =
        with(getAll()) {
            when {
                this.isEmpty() -> null
                this.size == 1 -> this.first()
                else -> this[Random.nextInt(0, this.size - 1)]
            }
        }

    override suspend fun get(uid: String): Expression? =
        getRaw(uid)?.toObject(Expression::class.java)

    private suspend fun getRaw(uid: String): DocumentSnapshot? =
        expressionsCollection
            .whereEqualTo("uid", uid)
            .get()
            .await()
            .documents
            .firstOrNull()

}
