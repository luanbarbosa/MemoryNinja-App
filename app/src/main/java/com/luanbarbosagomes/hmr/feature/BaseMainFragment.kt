package com.luanbarbosagomes.hmr.feature

import androidx.fragment.app.Fragment
import com.luanbarbosagomes.hmr.feature.main.ActivityMain

abstract class BaseMainFragment: Fragment() {

    internal val mainActivity: ActivityMain
        get() = activity as ActivityMain
}