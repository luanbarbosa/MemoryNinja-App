package com.luanbarbosagomes.hmr.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

enum class Level(val value: Int) {
    NEW(0),
    BASIC(10),
    INTERMEDIATE(20),
    ADVANCED(30),
    KNOWN(40)
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

    constructor() : this(null, "", "", "", Level.NEW, Level.NEW.value)

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
            level: Level,
            currentLevel: Int = Level.NEW.value
        ): Expression {
            val expressionWithoutId = Expression(null, "", value, translation, level, currentLevel)
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