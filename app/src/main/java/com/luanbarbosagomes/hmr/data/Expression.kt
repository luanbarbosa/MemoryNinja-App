package com.luanbarbosagomes.hmr.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

enum class Level(val threshold: Int) {
    NEW(threshold = 100),
    BASIC(threshold = 60),
    INTERMEDIATE(threshold = 30),
    ADVANCED(threshold = 10),
    KNOWN(threshold = 0)
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

    constructor() : this(null, "", "", "", Level.NEW.threshold)

    fun hash() = "$value$translation".replace("\\s".toRegex(), "-")

    fun bumpKnowledgeLevel() {
        if (currentLevel > 0) {
            currentLevel -= knowledgeJump
        }
    }

    fun downgradeKnowledgeLevel() {
        // harsher punishment for higher levels
        currentLevel += when {
            currentLevel == Level.NEW.threshold -> return
            level() == Level.KNOWN -> 5 * knowledgeJump
            level() == Level.ADVANCED -> 2 * knowledgeJump
            else -> knowledgeJump
        }
    }

    private fun level(): Level =
        when (currentLevel) {
            in 60..100 -> Level.NEW
            in 30..60 -> Level.BASIC
            in 10..30 -> Level.INTERMEDIATE
            in 1..10 -> Level.ADVANCED
            else -> Level.KNOWN
        }

    override fun toString(): String = "$value -> $translation"

    companion object {
        fun create(
            value: String,
            translation: String,
            level: Level
        ): Expression {
            val expressionWithoutId = Expression(null, "", value, translation, level.threshold)
            return expressionWithoutId.apply { uid = expressionWithoutId.hash() }
        }

        val knowledgeJump = 1
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