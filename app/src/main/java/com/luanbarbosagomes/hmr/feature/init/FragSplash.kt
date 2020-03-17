package com.luanbarbosagomes.hmr.feature.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.utils.withDelay

class FragSplash : BaseMainFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withDelay(1000) {
            // TODO - add logic to go to main if already logged in
            findNavController().navigate(
                FragSplashDirections.actionFragSplashToFragLogin(),
                NavOptions.Builder()
                    .setPopUpTo(R.id.fragSplash, true)
                    .build()
            )
        }
    }
}