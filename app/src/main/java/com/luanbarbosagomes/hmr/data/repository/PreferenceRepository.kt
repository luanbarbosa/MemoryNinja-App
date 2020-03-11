package com.luanbarbosagomes.hmr.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.luanbarbosagomes.hmr.feature.preference.StorageOption
import javax.inject.Inject

class PreferenceRepository @Inject constructor(private val preference: SharedPreferences) {

    var storageOption: StorageOption?
        set(value) = preference.edit { putInt(StorageOptionId, value?.id ?: Unknown) }
        get() = StorageOption.toValue(preference.getInt(StorageOptionId, Unknown))

    companion object {
        const val StorageOptionId = "storageOption"
        const val Unknown = -1
    }
}