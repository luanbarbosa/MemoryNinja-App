package com.luanbarbosagomes.hmr.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel.Result
import com.luanbarbosagomes.hmr.feature.main.FragMain
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_login.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FragLogin: BaseMainFragment() {

    private val authModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false).also {
            setupUi(it)
            subscribeToData()
        }
    }

    private fun subscribeToData() {
        authModel
            .data
            .observe(viewLifecycleOwner, Observer { updateUi(it) })
    }

    private fun updateUi(result: Result) {
        when (result) {
            is Result.Success -> mainActivity.loggedIn()
            is Result.Error -> showError(result.error)
        }
    }

    private fun showError(error: Throwable) {
        error.message?.toastIt()
    }

    private fun setupUi(root: View) {
        root.sendBtn.setOnClickListener {
            root.loginPhoneNumberEt.textOrNull()?.let {
                authModel.loginWithPhoneNumber(it)
            }
        }
    }

    companion object {
        val new = FragLogin()
    }
}

private fun EditText.textOrNull(): String? = if (text.toString().isEmpty()) null else text.toString()
