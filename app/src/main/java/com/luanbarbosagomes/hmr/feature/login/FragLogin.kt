package com.luanbarbosagomes.hmr.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.main.ActivityMain
import com.luanbarbosagomes.hmr.feature.main.FragMain
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_login.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class FragLogin: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false).also {
            setupUi(it)
        }
    }

    private fun setupUi(root: View) {
        root.sendBtn.setOnClickListener {
            root.loginPhoneNumberEt.textOrNull()?.let {
                loginUsingPhoneNumber(it)
            }
        }
    }

    private fun loginUsingPhoneNumber(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(error: FirebaseException) {
                    "Unable to login!".toastIt()
                }

                private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
                    FirebaseAuth
                        .getInstance()
                        .signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                (activity as ActivityMain).showScreen(FragMain.new)
                            } else {
                                Timber.w(it.exception)
                                "Unable to login!".toastIt()
                            }
                        }
                }
            })
    }

    companion object {
        val new = FragLogin()
    }
}

private fun EditText.textOrNull(): String? = if (text.toString().isEmpty()) null else text.toString()
