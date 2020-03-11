package com.luanbarbosagomes.hmr.feature.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.TransactionState
import com.luanbarbosagomes.hmr.feature.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_storage_option.view.*

class FragStorageOption : BaseMainFragment() {

    private val mainSharedModel by activityViewModels<MainViewModel>()
    private val preferenceViewModel by viewModels<PreferenceViewModel>()

    private lateinit var rootView: View

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
            TransactionState.Success -> mainSharedModel.preferenceSet()
            is TransactionState.Fail -> mainSharedModel.onError(state.error)
        }
    }

    companion object {
        val new = FragStorageOption()
    }
}
