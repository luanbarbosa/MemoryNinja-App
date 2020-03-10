package com.luanbarbosagomes.hmr.feature.login

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.repository.AuthRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthViewModel @Inject constructor() : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var authRepository: AuthRepository

    val state: MutableLiveData<State> = MutableLiveData()

    override fun onError(throwable: Throwable) = state.postValue(State.Error(throwable))

    override fun beforeRun() = state.postValue(State.Loading)

    private fun onSuccessfulLogin() = state.postValue(State.Success)

    fun logout() = authRepository.logout()

    fun loginWithPhoneNumber(phoneNumber: String) {
        launch {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) =
                        signInWithCredential(credential)

                    override fun onVerificationFailed(error: FirebaseException) =
                        onError(UnableToLoginException(error.localizedMessage ?: ""))

                }
            )
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        launch {
            FirebaseAuth
                .getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) onSuccessfulLogin()
                    else onError(UnableToLoginException(it.exception?.localizedMessage ?: ""))
                }
        }
    }

    sealed class State {
        object Success : State()
        data class Error(val error: Throwable) : State()
        object Loading : State()
    }
}

class UnableToLoginException(message: String) : Throwable(message = message)
