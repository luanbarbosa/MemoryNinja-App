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

class AuthViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var authRepository: AuthRepository

    val data: MutableLiveData<Result> = MutableLiveData()

    override fun onError(throwable: Throwable) = data.postValue(Result.Error(throwable))

    private fun onSuccessfulLogin() = data.postValue(Result.Success)

    fun logout() = authRepository.logout()

    fun loginWithPhoneNumber(phoneNumber: String) {
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

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        FirebaseAuth
            .getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) onSuccessfulLogin()
                else onError(UnableToLoginException(it.exception?.localizedMessage ?: ""))
            }
    }

    sealed class Result {
        object Success : Result()
        data class Error(val error: Throwable) : Result()
    }
}

class UnableToLoginException(message: String) : Throwable(message = message)
