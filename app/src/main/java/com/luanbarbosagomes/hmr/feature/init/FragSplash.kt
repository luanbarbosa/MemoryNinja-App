package com.luanbarbosagomes.hmr.feature.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.init.InitViewModel.State
import com.luanbarbosagomes.hmr.utils.withDelay

class FragSplash : BaseMainFragment() {

    private val initViewModel by viewModels<InitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false).also {
        observeToData()
    }

    private fun observeToData() {
        initViewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            State.Loading -> {}
            State.LoginNeeded ->
                navigate(FragSplashDirections.actionFragSplashToFragLogin())
            State.StorageOptionNeeded ->
                navigate(FragSplashDirections.actionFragSplashToFragStorageOption())
            State.Initiated ->
                navigate(FragSplashDirections.actionFragSplashToFragMain())
            is State.Error -> navigate(
                FragSplashDirections.actionFragSplashToFragError(state.error.localizedMessage)
            )
        }
    }

    private fun navigate(direction: NavDirections) {
        withDelay(2000) {
            navigateTo(
                direction,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.fragSplash, true)
                    .build()
            )
        }
    }
}
