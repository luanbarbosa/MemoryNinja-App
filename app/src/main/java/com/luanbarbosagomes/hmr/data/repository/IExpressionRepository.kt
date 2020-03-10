package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression

interface IExpressionRepository {

    suspend fun save(expression: Expression)

    suspend fun getAll(): List<Expression>

    suspend fun deleteAll()

    suspend fun getRandom(): Expression?

    suspend fun get(uid: Long): Expression?
}