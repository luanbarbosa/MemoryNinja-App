package com.luanbarbosagomes.hmr.data.database

import androidx.room.*
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.data.Expression

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

}
