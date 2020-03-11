package com.luanbarbosagomes.hmr.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Expression.ExpressionIdentifier
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.details.ExpressionViewModel.State
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_expression_details.view.*

class FragExpressionDetails private constructor(
    private val identifier: ExpressionIdentifier
) : BaseMainFragment() {

    private val model by viewModels<ExpressionViewModel>()

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_expression_details, container, false)
        subscribe()
        model.retrieveExpression(identifier)
        return root
    }

    private fun subscribe() {
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
        root.apply {
            expressionTv.text = expression.value
            translationTv.text = expression.translation
        }
    }

    private fun showError(error: Throwable) {
        "Error! ${error.message}".toastIt()
    }

    companion object {
        fun newFromLocal(id: Long) = FragExpressionDetails(
            ExpressionIdentifier(expressionLocalId = id)
        )

        fun newFromRemote(id: String) = FragExpressionDetails(
            ExpressionIdentifier(expressionRemoteId = id)
        )
    }
}
