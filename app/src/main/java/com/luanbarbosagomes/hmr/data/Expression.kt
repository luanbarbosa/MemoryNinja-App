package com.luanbarbosagomes.hmr.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

enum class Level(val priority: Int) {
    NEW(priority = 100),
    BASIC(priority = 60),
    INTERMEDIATE(priority = 30),
    ADVANCED(priority = 10),
    KNOWN(priority = 0)
}

@Entity(tableName = "expression")
@Parcelize
class Expression(
    @PrimaryKey(autoGenerate = true) var primaryKey: Long? = null,
    @ColumnInfo var uid: String,
    @ColumnInfo var value: String,
    @ColumnInfo var translation: String,
    @ColumnInfo var currentLevel: Int
) : Parcelable {

    constructor() : this(null, "", "", "", Level.NEW.priority)

    fun hash() = "$value$translation".replace("\\s".toRegex(), "-")

    fun level() =
        when (currentLevel) {
            in 100 downTo 60 -> Level.NEW
            in 60 downTo 30 -> Level.BASIC
            in 30 downTo 10 -> Level.INTERMEDIATE
            in 10 downTo 0 -> Level.ADVANCED
            else -> Level.KNOWN
        }

    override fun toString(): String =
        """Expression
            |uid:$uid, 
            |value:$value, 
            |translation:$translation, 
            |current:$currentLevel""".trimMargin()

    companion object {
        fun create(
            value: String,
            translation: String,
            level: Level
        ): Expression {
            val expressionWithoutId = Expression(null, "", value, translation, level.priority)
            return expressionWithoutId.apply { uid = expressionWithoutId.hash() }
        }
    }
}

fun Expression.copy(
    uid: String? = null,
    value: String? = null,
    translation: String? = null,
    currentLevel: Int? = null
): Expression = this.apply {
    uid?.let { this.uid = uid }
    value?.let { this.value = value }
    translation?.let { this.translation = translation }
    currentLevel?.let { this.currentLevel = currentLevel }
}