package com.luanbarbosagomes.hmr.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level

@Database(entities = [Expression::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expressionDao(): ExpressionDao
}

object Converters {
    @JvmStatic
    @TypeConverter
    fun toLevel(value: Int): Level = when (value) {
        1 -> Level.POOR
        2 -> Level.BASIC
        3 -> Level.INTERMEDIATE
        4 -> Level.KNOWN
        else -> Level.NEW
    }

    @JvmStatic
    @TypeConverter
    fun fromLevel(level: Level) = level.uid
}