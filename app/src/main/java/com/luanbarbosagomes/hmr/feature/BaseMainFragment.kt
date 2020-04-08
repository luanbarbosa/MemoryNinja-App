package com.luanbarbosagomes.hmr.feature

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

abstract class BaseMainFragment: Fragment() {

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