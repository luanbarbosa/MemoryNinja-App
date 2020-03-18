package com.luanbarbosagomes.hmr.feature.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.TransactionState
import kotlinx.android.synthetic.main.fragment_storage_option.view.*

class FragStorageOption : BaseMainFragment() {

    private val preferenceViewModel by viewModels<PreferenceViewModel>()

    private lateinit var rootView: View

    private val notOnStack: NavOptions = NavOptions.Builder()
        .setPopUpTo(R.id.fragStorageOption, true)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_storage_option, container, false).also {
            rootView = it
            setupUi()
            subscribeToData()
        }
    }

    private fun setupUi() {
        with(rootView) {
            cloudBtn.setOnClickListener {
                preferenceViewModel.updateStorageOption(StorageOption.REMOTE)
            }
            localBtn.setOnClickListener {
                preferenceViewModel.updateStorageOption(StorageOption.LOCAL)
            }
        }
    }

    private fun subscribeToData() {
        preferenceViewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: TransactionState) {
        when (state) {
            TransactionState.Success -> {
                navigateTo(
                    FragStorageOptionDirections.actionFragStorageOptionToFragMain(),
                    navOptions = notOnStack
                )
            }
            is TransactionState.Fail -> {
                navigateTo(
                    FragStorageOptionDirections.actionFragStorageOptionToFragError(
                        errorMsg = state.error.localizedMessage
                    )
                )
            }
        }
    }

}
