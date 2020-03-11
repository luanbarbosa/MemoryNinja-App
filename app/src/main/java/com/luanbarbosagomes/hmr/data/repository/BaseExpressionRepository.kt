package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import javax.inject.Inject

open class BaseExpressionRepository @Inject constructor() {

    open suspend fun save(expression: Expression) {}

    open suspend fun getAll(): List<Expression> = listOf()

    open suspend fun deleteAll() {}

    open suspend fun getRandom(): Expression? = null

    open suspend fun get(uid: String): Expression? = null
}