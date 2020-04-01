package com.luanbarbosagomes.hmr.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.ExpressionDao

@Database(entities = [Expression::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expressionDao(): ExpressionDao
}
