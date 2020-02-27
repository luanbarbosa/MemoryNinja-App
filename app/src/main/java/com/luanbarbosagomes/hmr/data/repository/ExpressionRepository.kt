package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.database.AppDatabase

class ExpressionRepository(private val database: AppDatabase) {

    suspend fun save(expression: Expression) {
        database.expressionDao().insert(expression)
    }

    suspend fun getAll() = database.expressionDao().getAll()

    suspend fun deleteAll() = database.expressionDao().deleteAll()
}