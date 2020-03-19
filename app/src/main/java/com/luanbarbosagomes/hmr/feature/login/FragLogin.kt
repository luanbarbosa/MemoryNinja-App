package com.luanbarbosagomes.hmr.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel.State
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.fragment_login.view.*

class FragLogin: BaseMainFragment() {

    private val authModel by viewModels<AuthViewModel>()

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false).also {
            rootView = it
            setupUi()
            subscribeToData()
        }
    }

    private fun subscribeToData() {
        authModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            is State.Success -> {
                navigate(FragLoginDirections.actionFragLoginToFragStorageOption())
            }
            is State.Error -> {
                rootView.progressIndicator.hide()
                navigate(
                    FragLoginDirections.actionFragLoginToFragError(
                        errorMsg = state.error.localizedMessage
                    ),
                    keepOnStack = true
                )
            }
            is State.Loading -> rootView.progressIndicator.show()
        }
    }

    private fun navigate(directions: NavDirections, keepOnStack: Boolean = false) {
        navigateTo(
            directions,
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragLogin, !keepOnStack)
                .build()
        )
    }

    private fun setupUi() {
        with (rootView) {
            loginBtn.setOnClickListener {
                loginPhoneNumberEt.textOrNull()?.let {
                    authModel.loginWithPhoneNumber(it)
                }
            }
            guestBtn.setOnClickListener {
                authModel.loginAsGuest()
            }
            progressIndicator.hide()
        }
    }

}

private fun EditText.textOrNull(): String? = if (text.toString().isEmpty()) null else text.toString()
