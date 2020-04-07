package com.luanbarbosagomes.hmr.feature.preference

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.expression.list.ExpressionsViewModel
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsBottomSheet : BottomSheetDialogFragment() {

    private val preferenceViewModel by viewModels<PreferenceViewModel>()

    private val expressionViewModel by activityViewModels<ExpressionsViewModel>()

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false).also {
        rootView = it
        setupUi()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        expressionViewModel.reload()
    }

    private fun setupUi() {
        with(rootView) {
            logoutBtn.setOnClickListener {
                logout()
            }

            quizFrequencyBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener() {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    quizFrequencyDescriptor.text = getString(
                        R.string.settings_quiz_frequency_sub_label_format, progress
                    )
                    preferenceViewModel.updateQuizFrequency(progress)
                }
            })
            quizFrequencyBar.progress = preferenceViewModel.quizFrequencyPreference()
        }
    }

    private fun logout() {
        preferenceViewModel.logout()
        navigateTo(
            SettingsBottomSheetDirections.actionFragSettingsToSplash(),
            navOptions = NavOptions.Builder().setPopUpTo(R.id.settingsBottomSheet, true)
                .build()
        )
    }

    private fun navigateTo(direction: NavDirections, navOptions: NavOptions? = null) {
        findNavController().navigate(direction, navOptions)
    }

    open inner class OnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }
}
