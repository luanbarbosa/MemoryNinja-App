package com.luanbarbosagomes.hmr.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.luanbarbosagomes.hmr.App.Companion.firebaseAuth
import com.luanbarbosagomes.hmr.data.repository.AuthRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State>
        get() = _state

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    override fun beforeRun() = _state.postValue(State.Loading)

    private fun onSuccessfulLogin() = _state.postValue(State.Success)

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
            firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) onSuccessfulLogin()
                    else onError(UnableToLoginException(it.exception?.localizedMessage ?: ""))
                }
        }
    }

    fun loginAsGuest() {
        launch {
            firebaseAuth
                .signInAnonymously()
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
