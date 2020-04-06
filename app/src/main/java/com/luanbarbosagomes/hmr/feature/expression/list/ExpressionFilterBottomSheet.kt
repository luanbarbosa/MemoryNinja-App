package com.luanbarbosagomes.hmr.feature.expression.list

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import kotlinx.android.synthetic.main.fragment_expression_filter.*
import kotlinx.android.synthetic.main.fragment_expression_filter.view.*
import kotlinx.android.synthetic.main.fragment_expression_filter.view.chipNew
import timber.log.Timber

class ExpressionFilterBottomSheet : BottomSheetDialogFragment() {

    private val preferenceViewModel by viewModels<PreferenceViewModel>()

    private val expressionViewModel by viewModels<ExpressionsViewModel>()

    lateinit var rootView: View

    private val checkChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        preferenceViewModel.updateExpressionFilterPreference(buildFilterList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_expression_filter, container, false).also {
        rootView = it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUi()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        expressionViewModel.reload()
    }

    private fun setupUi() {
        preferenceViewModel.preferenceRepository.filterExpressionBy.forEach {
            when (it) {
                Level.NEW -> chipNew.isChecked = true
                Level.BASIC -> chipBasic.isChecked = true
                Level.INTERMEDIATE -> chipIntermediate.isChecked = true
                Level.ADVANCED -> chipAdvanced.isChecked = true
                Level.KNOWN -> chipKnown.isChecked = true
            }
        }

        with(rootView) {
            // ChipGroup does not trigger check changed listener for Filter options so far
            chipNew.setOnCheckedChangeListener(checkChangedListener)
            chipBasic.setOnCheckedChangeListener(checkChangedListener)
            chipIntermediate.setOnCheckedChangeListener(checkChangedListener)
            chipAdvanced.setOnCheckedChangeListener(checkChangedListener)
            chipKnown.setOnCheckedChangeListener(checkChangedListener)
        }
    }

    private fun buildFilterList(): List<Level> {
        val filterBy = mutableListOf<Level>()
        with(rootView) {
            filterBy.addIf(Level.NEW, chipNew.isChecked)
            filterBy.addIf(Level.BASIC, chipBasic.isChecked)
            filterBy.addIf(Level.INTERMEDIATE, chipIntermediate.isChecked)
            filterBy.addIf(Level.ADVANCED, chipAdvanced.isChecked)
            filterBy.addIf(Level.KNOWN, chipKnown.isChecked)
        }
        return filterBy
    }
}

private fun <E> MutableList<E>.addIf(new: E, conditional: Boolean) {
    if (conditional) add(new)
}
