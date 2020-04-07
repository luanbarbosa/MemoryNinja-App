package com.luanbarbosagomes.hmr.feature.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.PreferenceRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.preference.TransactionState.Fail
import com.luanbarbosagomes.hmr.feature.preference.TransactionState.Success
import com.luanbarbosagomes.hmr.work.NotificationWorker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceViewModel @Inject constructor(): BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var authViewModel: AuthViewModel

    private val _state: MutableLiveData<TransactionState> = MutableLiveData()

    val state: LiveData<TransactionState>
        get() = _state

    override fun onError(error: Throwable) = _state.postValue(Fail(error))

    fun updateStorageOption(storageOption: StorageOption) {
        preferenceRepository.storageOption = storageOption
        _state.postValue(Success)
    }

    fun updateExpressionFilterPreference(filterBy: List<Level>) {
        preferenceRepository.filterExpressionBy = filterBy
    }

    fun storageOptionSet() = preferenceRepository.storageOption != null

    fun logout() {
        // we assume that the logout will be executed successfully no matter what
        preferenceRepository.storageOption = null
        authViewModel.logout()
    }

    fun quizFrequencyPreference(): Int = preferenceRepository.quizFrequency

    fun updateQuizFrequency(newFrequency: Int) {
        preferenceRepository.quizFrequency = newFrequency
        NotificationWorker.scheduleQuiz(frequency = newFrequency.toLong())
    }
}

enum class StorageOption(val id: Int) {
    LOCAL(0), REMOTE(1);

    companion object {
        fun toValue(id: Int): StorageOption? = when (id) {
            0 -> LOCAL
            1 -> REMOTE
            else -> null
        }
    }
}

sealed class TransactionState {
    object Success: TransactionState()
    data class Fail(val error: Throwable): TransactionState()
}