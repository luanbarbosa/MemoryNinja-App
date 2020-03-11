package com.luanbarbosagomes.hmr.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

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
    open var uid: Long? = null
    @Ignore
    open lateinit var value: String
    @Ignore
    open lateinit var translation: String
    @Ignore
    open lateinit var level: Level

    fun toExpression() = Expression(uid, value, translation, level)
}

@Entity(tableName = "expression")
data class Expression(
    @PrimaryKey(autoGenerate = true) override var uid: Long? = null,
    @ColumnInfo override var value: String,
    @ColumnInfo override var translation: String,
    @ColumnInfo override var level: Level
) : ExpressionLean() {

    @Ignore
    var uidString: String? = null

    @Parcelize
    data class ExpressionIdentifier(
        val expressionLocalId: Long? = null,
        val expressionRemoteId: String? = null
    ) : Parcelable

    companion object {
        val empty = Expression(value = "", translation = "", level = Level.NEW)
    }
}