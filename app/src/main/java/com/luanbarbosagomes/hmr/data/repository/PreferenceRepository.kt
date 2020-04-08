package com.luanbarbosagomes.hmr.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.feature.preference.StorageOption
import javax.inject.Inject

class PreferenceRepository @Inject constructor(private val preference: SharedPreferences) {

    var storageOption: StorageOption?
        set(value) = preference.edit { putInt(StorageOptionId, value?.id ?: Unknown) }
        get() = StorageOption.toValue(preference.getInt(StorageOptionId, Unknown))

    var filterExpressionBy: List<Level>
        set(value) = preference.edit {
            putStringSet(
                FilterExpressionsById,
                value.map { it.name }.toSet()
            )
        }
        get() = preference.getStringSet(FilterExpressionsById, setOf())
            ?.map { Level.toValue(it) ?: Level.NEW } ?: DefaultFilterExpressionByPreference

    var quizFrequency: Int
        set(value) = preference.edit { putInt(QuizFrequencyID, value) }
        get() = preference.getInt(QuizFrequencyID, DefaultQuizFrequency)

    fun clearPreferences() {
        storageOption = null
        filterExpressionBy = DefaultFilterExpressionByPreference
        quizFrequency = DefaultQuizFrequency
    }

    companion object {
        const val StorageOptionId = "storageOption"
        const val FilterExpressionsById = "filterExpressionsBy"
        const val QuizFrequencyID = "quizFrequency"
        const val DefaultQuizFrequency = 8
        val DefaultFilterExpressionByPreference = Level.all
        const val Unknown = -1
    }
}