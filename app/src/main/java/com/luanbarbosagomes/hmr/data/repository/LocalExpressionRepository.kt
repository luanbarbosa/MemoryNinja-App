package com.luanbarbosagomes.hmr.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import javax.inject.Inject

/**
 * Expression repository responsible for CRUD expressions locally using Room (SQLite).
 */
class LocalExpressionRepository @Inject constructor(
    private val database: AppDatabase
): IExpressionRepository {

    private val dao: ExpressionDao
        get() = database.expressionDao()

    override suspend fun save(expression: Expression) = dao.insert(expression)

    override suspend fun getAll() = dao.getAll()

    override suspend fun deleteAll() = dao.deleteAll()

    override suspend fun getRandom() = dao.getRandom()

    override suspend fun get(uid: Long) = dao.get(uid)
}

@Dao
interface ExpressionDao {

    @Query("SELECT * FROM expression")
    suspend fun getAll(): List<Expression>

    @Insert
    suspend fun insert(vararg expressions: Expression)

    @Delete
    suspend fun delete(expression: Expression)

    @Query("DELETE FROM expression")
    suspend fun deleteAll()

    @Query("SELECT * FROM expression ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandom(): Expression?

    @Query("SELECT * FROM expression WHERE uid = :id")
    suspend fun get(id: Long): Expression?

}