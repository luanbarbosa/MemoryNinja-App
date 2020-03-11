package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression

abstract class BaseExpressionRepository {

    abstract suspend fun save(expression: Expression)

    abstract suspend fun getAll(): List<Expression>

    abstract suspend fun deleteAll()

    abstract suspend fun getRandom(): Expression?

    open suspend fun get(uid: Long): Expression? = null

    open suspend fun get(stringUid: String): Expression? = null
}