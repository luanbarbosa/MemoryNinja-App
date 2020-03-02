package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.data.database.ExpressionDao

class ExpressionRepository(private val database: AppDatabase) {

    private val dao: ExpressionDao
        get() = database.expressionDao()

    suspend fun save(expression: Expression) = dao.insert(expression)

    suspend fun getAll() = dao.getAll()

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun getRandom() = dao.getRandom()

    suspend fun get(id: Long) = dao.get(id)
}