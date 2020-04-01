package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import javax.inject.Inject

open class BaseExpressionRepository @Inject constructor() {

    open suspend fun save(expression: Expression) {}

    open suspend fun update(expression: Expression) {}

    open suspend fun getAll(): List<Expression> = listOf()

    open suspend fun deleteAll() {}

    open suspend fun delete(expression: Expression) {}

    open suspend fun getRandom(): Expression? = null

    open suspend fun get(uid: String): Expression? = null

    open suspend fun updateLevel(uid: String, correctAnswer: Boolean) {}

}