package com.luanbarbosagomes.hmr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Level(val uid: Int) {
    NEW(0),
    POOR(1),
    BASIC(2),
    INTERMEDIATE(3),
    KNOWN(4)
}

@Entity(tableName = "expression")
data class Expression(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo val value: String,
    @ColumnInfo val translation: String,
    @ColumnInfo val level: Level
)