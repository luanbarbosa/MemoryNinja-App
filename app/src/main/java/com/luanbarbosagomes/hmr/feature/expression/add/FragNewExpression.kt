package com.luanbarbosagomes.hmr.feature.expression.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.expression.FragBaseEditExpression
import com.luanbarbosagomes.hmr.feature.expression.add.NewExpressionViewModel.State
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragNewExpression : FragBaseEditExpression() {

    private val viewModel by viewModels<NewExpressionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_new_expression, container, false).also {
        rootView = it
        setupUi()
        observeData()
    }

    override fun setupUi() {
        with (rootView) {
            _chipNew = chipNew
            _chipBasic = chipBasic
            _chipIntermediate = chipIntermediate
            _chipAdvanced = chipAdvanced
            _chipKnown = chipKnown
            _levelGroup = levelGroup
            _expressionEt = expressionEt
            _translationEt = translationEt
            _saveBtn = saveBtn
        }
        super.setupUi()
    }

    override fun save() {
        viewModel.saveExpression(
            _expressionEt.text.toString(),
            _translationEt.text.toString(),
            getSelectedLevel()
        )
    }

    override fun afterSuccessfulSave() {
        hideSuccessfulIndicator()
    }

    private fun observeData() {
        viewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            State.Success -> {
                showSuccessStatus()
                clearFields()
            }
            is State.Error -> {
                navigateTo(
                    FragNewExpressionDirections.actionFragNewExpressionToFragError(
                        errorMsg = state.error.localizedMessage
                    )
                )
            }
        }
    }

}
