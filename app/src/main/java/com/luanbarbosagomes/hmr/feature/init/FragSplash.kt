package com.luanbarbosagomes.hmr.feature.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val direction = when (initViewModel.currentState()) {
            State.LoginNeeded -> FragSplashDirections.actionFragSplashToFragLogin()
            State.StorageOptionNeeded -> FragSplashDirections.actionFragSplashToFragStorageOption()
            State.Initiated -> FragSplashDirections.actionFragSplashToFragMain()
        }

        withDelay(1000) {
            navigateTo(
                direction,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.fragSplash, true)
                    .build()
            )
        }
    }
}