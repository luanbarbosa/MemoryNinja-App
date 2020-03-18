package com.luanbarbosagomes.hmr.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.details.ExpressionViewModel.State
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_expression_details.view.*

class FragExpressionDetails : BaseMainFragment() {

    private val model by viewModels<ExpressionViewModel>()

    private val args by navArgs<FragExpressionDetailsArgs>()

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_expression_details, container, false).also {
        rootView = it
        observeData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.retrieveExpression(args.expressionUid)
    }

    private fun observeData() {
        model.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            is State.Success -> showExpression(state.expression)
            is State.Error -> showError(state.error)
        }
    }

    private fun showExpression(expression: Expression) {
        rootView.apply {
            expressionTv.text = expression.value
            translationTv.text = expression.translation
        }
    }

    private fun showError(error: Throwable) {
        "Error! ${error.message}".toastIt()
    }

}
