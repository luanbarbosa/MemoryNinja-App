package com.luanbarbosagomes.hmr.feature.expression.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel.State
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel.State.Error
import com.luanbarbosagomes.hmr.feature.expression.FragBaseEditExpression
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragEditExpression : FragBaseEditExpression() {

    private val viewModel by viewModels<ExpressionViewModel> { viewModelFactory }

    private val args by navArgs<FragEditExpressionArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_new_expression, container, false).also {
        rootView = it
        setupUi()
        observeData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadExpression(args.expressionUid)
    }

    override fun setupUi() {
        with (rootView) {
            titleTv.text = getString(R.string.edit_expression_title)
            _expressionEt = expressionEt
            _translationEt = translationEt
            _saveBtn = saveBtn
        }
        super.setupUi()
    }

    override fun save() {
        viewModel.updateExpression(
            _expressionEt.text.toString(),
            _translationEt.text.toString(),
            getSelectedLevel()
        )
    }

    override fun afterSuccessfulSave() {
        navigateBack()
    }

    private fun observeData() {
        viewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            State.Saved -> showSuccessStatus()
            is State.Loaded -> showExpression(state.expression)
            is Error -> {
                navigateTo(
                    FragEditExpressionDirections.actionFragNewExpressionToFragError(
                        errorMsg = state.error.localizedMessage
                    )
                )
            }
        }
    }

    private fun showExpression(expression: Expression) {
        with (expression) {
            _expressionEt.setText(value)
            _translationEt.setText(translation)
            selectLevel(level())
        }
    }

}
