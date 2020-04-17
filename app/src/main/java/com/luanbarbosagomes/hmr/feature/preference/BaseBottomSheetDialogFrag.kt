package com.luanbarbosagomes.hmr.feature.preference

import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luanbarbosagomes.hmr.App
import javax.inject.Inject

open class BaseBottomSheetDialogFrag: BottomSheetDialogFragment() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
}