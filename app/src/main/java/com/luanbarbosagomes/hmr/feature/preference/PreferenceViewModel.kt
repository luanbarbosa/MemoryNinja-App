package com.luanbarbosagomes.hmr.feature.preference

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.repository.PreferenceRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.TransactionState
import com.luanbarbosagomes.hmr.feature.TransactionState.Fail
import com.luanbarbosagomes.hmr.feature.TransactionState.Success
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
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

    val state: MutableLiveData<TransactionState> = MutableLiveData()

    override fun onError(error: Throwable) = state.postValue(Fail(error))

    fun updateStorageOption(storageOption: StorageOption) {
        preferenceRepository.storageOption = storageOption
        state.postValue(Success)
    }

    fun storageOptionSet() = preferenceRepository.storageOption != null

    fun logout() {
        // we assume that the logout will be executed successfully no matter what
        authViewModel.logout()
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