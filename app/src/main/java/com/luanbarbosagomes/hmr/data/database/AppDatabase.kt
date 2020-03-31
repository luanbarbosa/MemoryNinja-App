package com.luanbarbosagomes.hmr.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.ExpressionDao

@Database(entities = [Expression::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expressionDao(): ExpressionDao
}

object Converters {
    @JvmStatic
    @TypeConverter
    fun toLevel(value: Int): Level = when (value) {
        Level.NEW.id -> Level.NEW
        Level.BASIC.id -> Level.BASIC
        Level.INTERMEDIATE.id -> Level.INTERMEDIATE
        Level.ADVANCED.id -> Level.ADVANCED
        else -> Level.KNOWN
    }

    @JvmStatic
    @TypeConverter
    fun fromLevel(level: Level) = level.id
}