package com.luanbarbosagomes.hmr.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.luanbarbosagomes.hmr.data.Expression

@Dao
interface ExpressionDao {

    @Query("SELECT * FROM expression")
    fun getAll(): List<Expression>

    @Insert
    fun insert(vararg expressions: Expression)

    @Delete
    fun delete(expression: Expression)
}