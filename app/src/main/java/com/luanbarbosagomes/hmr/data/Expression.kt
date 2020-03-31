package com.luanbarbosagomes.hmr.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

enum class Level(val id: Int, val priorityPercentage: Int) {
    NEW(id = 1, priorityPercentage = 70),
    BASIC(id = 2, priorityPercentage = 15),
    INTERMEDIATE(id = 3, priorityPercentage = 10),
    ADVANCED(id = 4, priorityPercentage = 3),
    KNOWN(id = 5, priorityPercentage = 2)
}

@Entity(tableName = "expression")
@Parcelize
class Expression(
    @PrimaryKey(autoGenerate = true) var primaryKey: Long? = null,
    @ColumnInfo var uid: String,
    @ColumnInfo var value: String,
    @ColumnInfo var translation: String,
    @ColumnInfo var level: Level,
    @ColumnInfo var currentLevel: Int
) : Parcelable {

    constructor() : this(null, "", "", "", Level.NEW, Level.NEW.priorityPercentage)

    fun hash() = "$value$translation".replace("\\s".toRegex(), "-")

    override fun toString(): String =
        """Expression
            |uid:$uid, 
            |value:$value, 
            |translation:$translation, 
            |level:$level, 
            |current:$currentLevel""".trimMargin()

    companion object {
        fun create(
            value: String,
            translation: String,
            level: Level
        ): Expression {
            val expressionWithoutId = Expression(null, "", value, translation, level, level.priorityPercentage)
            return expressionWithoutId.apply { uid = expressionWithoutId.hash() }
        }
    }
}

fun Expression.copy(
    uid: String? = null,
    value: String? = null,
    translation: String? = null,
    level: Level? = null
): Expression = this.apply {
    uid?.let { this.uid = uid }
    value?.let { this.value = value }
    translation?.let { this.translation = translation }
    level?.let { this.level = level }
}