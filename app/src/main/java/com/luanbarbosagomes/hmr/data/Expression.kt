package com.luanbarbosagomes.hmr.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

enum class Level(val uid: Int) {
    NEW(0),
    POOR(1),
    BASIC(2),
    INTERMEDIATE(3),
    KNOWN(4)
}

@Entity(tableName = "expression")
@Parcelize
class Expression(
    @PrimaryKey(autoGenerate = true) var primaryKey: Long? = null,
    @ColumnInfo var uid: String,
    @ColumnInfo var value: String,
    @ColumnInfo var translation: String,
    @ColumnInfo var level: Level
) : Parcelable {

    constructor(): this(null, "", "", "", Level.NEW)

    fun hash() = "$value$translation".replace("\\s".toRegex(), "-")

    override fun toString(): String =
        "Expression[uid:$uid, value:$value, translation:$translation, level:$level]"

    companion object {
        fun create(
            value: String,
            translation: String,
            level: Level
        ): Expression {
            val expressionWithoutId = Expression(null, "", value, translation, level)
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