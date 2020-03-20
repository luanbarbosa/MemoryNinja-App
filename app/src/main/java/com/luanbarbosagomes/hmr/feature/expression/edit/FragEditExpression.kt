package com.luanbarbosagomes.hmr.feature.expression.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel.State
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel.State.Error
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragEditExpression : BaseMainFragment() {

    private val viewModel by viewModels<ExpressionViewModel>()

    private val args by navArgs<FragEditExpressionArgs>()

    lateinit var rootView: View

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

    private fun setupUi() {
        rootView.saveBtn.setOnClickListener {
            viewModel.updateExpression(
                rootView.expressionEt.text.toString(),
                rootView.translationEt.text.toString()
            )
        }
    }

    private fun observeData() {
        viewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            State.Saved -> {
                "Saved!".toastIt()
                navigateBack()
            }
            is State.Loaded -> {
                with (state.expression) {
                    rootView.expressionEt.setText(value)
                    rootView.translationEt.setText(translation)
                }
            }
            is Error -> {
                navigateTo(
                    FragEditExpressionDirections.actionFragNewExpressionToFragError(
                        errorMsg = state.error.localizedMessage
                    )
                )
            }
        }
    }

}
