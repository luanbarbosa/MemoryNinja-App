package com.luanbarbosagomes.hmr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

enum class Level(val uid: Int) {
    NEW(0),
    POOR(1),
    BASIC(2),
    INTERMEDIATE(3),
    KNOWN(4)
}

/**
 * Class created to be a bridge object between locally stored expressions (SQLite-Room) and
 * remotely stored ones (Firebase Firestore).
 */
open class ExpressionLean {
    @Ignore
    open var primaryKey: Long? = null
    @Ignore
    open lateinit var uid: String
    @Ignore
    open lateinit var value: String
    @Ignore
    open lateinit var translation: String
    @Ignore
    open lateinit var level: Level

    fun toExpression() = Expression(primaryKey, uid, value, translation, level)
}

@Entity(tableName = "expression")
data class Expression(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Long? = null,
    @ColumnInfo override var uid: String,
    @ColumnInfo override var value: String,
    @ColumnInfo override var translation: String,
    @ColumnInfo override var level: Level
) : ExpressionLean() {

    @Ignore
    val hash = "$value$translation"

    companion object {
        fun create(
            value: String,
            translation: String,
            level: Level
        ): Expression {
            val expressionWithoutId = Expression(null, "", value, translation, level)
            return expressionWithoutId.copy(uid = expressionWithoutId.hash)
        }
    }
}