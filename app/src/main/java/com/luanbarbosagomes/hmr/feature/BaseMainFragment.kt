package com.luanbarbosagomes.hmr.feature

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.luanbarbosagomes.hmr.App
import javax.inject.Inject

abstract class BaseMainFragment: Fragment() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    fun navigateTo(direction: NavDirections, navOptions: NavOptions? = null) {
        findNavController().navigate(direction, navOptions)
    }

    fun navigateTo(direction: NavDirections, navExtras: Navigator.Extras) {
        findNavController().navigate(direction, navExtras)
    }

    fun navigateBack() {
        findNavController().popBackStack()
    }
}