package com.luanbarbosagomes.hmr.data.repository

import androidx.room.*
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import javax.inject.Inject
import kotlin.math.exp

/**
 * Expression repository responsible for CRUD expressions locally using Room (SQLite).
 */
class LocalExpressionRepository @Inject constructor(
    private val database: AppDatabase
) : BaseExpressionRepository() {

    private val dao: ExpressionDao
        get() = database.expressionDao()

    override suspend fun save(expression: Expression) = dao.insert(expression)

    override suspend fun update(expression: Expression) = dao.update(expression)

    override suspend fun getAll(limit: Boolean) = dao.getAll()

    override suspend fun deleteAll() = dao.deleteAll()

    override suspend fun delete(expression: Expression) = dao.delete(expression)

    override suspend fun getRandom() = dao.getRandom()

    override suspend fun get(uid: String): Expression? = dao.get(uid)

    override suspend fun updateLevel(uid: String, correctAnswer: Boolean) {
        get(uid)?.let {
            if (correctAnswer) it.bumpKnowledgeLevel()
            else it.downgradeKnowledgeLevel()
            update(it)
        }
    }

}

@Dao
interface ExpressionDao {

    @Query("SELECT * FROM expression")
    suspend fun getAll(): List<Expression>

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