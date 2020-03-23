package com.luanbarbosagomes.hmr.data.repository

import androidx.room.*
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import javax.inject.Inject

/**
 * Expression repository responsible for CRUD expressions locally using Room (SQLite).
 */
class LocalExpressionRepository @Inject constructor(
    private val database: AppDatabase
): BaseExpressionRepository() {

    private val dao: ExpressionDao
        get() = database.expressionDao()

    override suspend fun save(expression: Expression) = dao.insert(expression)

    override suspend fun update(expression: Expression) = dao.update(expression)

    override suspend fun getAll(level: Level?) = level?.let { getAllByLevel(it) } ?: getAll()

    private suspend fun getAll() = dao.getAll()

    private suspend fun getAllByLevel(level: Level) = dao.getAllByLevel(level)

    override suspend fun deleteAll() = dao.deleteAll()

    override suspend fun delete(expression: Expression) = dao.delete(expression)

    override suspend fun getRandom() = dao.getRandom()

    override suspend fun get(uid: String): Expression? = dao.get(uid)

}

@Dao
interface ExpressionDao {

    @Query("SELECT * FROM expression")
    suspend fun getAll(): List<Expression>

    @Query("SELECT * FROM expression WHERE level=:level")
    suspend fun getAllByLevel(level: Level): List<Expression>

    @Insert
    suspend fun insert(vararg expressions: Expression)

    @Update
    suspend fun update(expression: Expression)

    @Delete
    suspend fun delete(expression: Expression)

    @Query("DELETE FROM expression")
    suspend fun deleteAll()

    @Query("SELECT * FROM expression ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandom(): Expression?

    @Query("SELECT * FROM expression WHERE uid = :uid")
    suspend fun get(uid: String): Expression?

}