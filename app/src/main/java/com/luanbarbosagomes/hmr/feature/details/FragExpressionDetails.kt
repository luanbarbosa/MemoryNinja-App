package com.luanbarbosagomes.hmr.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import kotlinx.android.synthetic.main.fragment_expression_details.view.*

class FragExpressionDetails(private val expressionId: Long) : Fragment() {

    private val model by viewModels<ExpressionViewModel>()

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_expression_details, container, false)
        subscribe()
        model.retrieveExpression(expressionId)
        return root
    }

    private fun subscribe() {
        model
            .data
            .observe(viewLifecycleOwner, Observer { updateUi(it) })
    }

    private fun updateUi(result: Result) {
        when (result) {
            is Result.Success -> showExpression(result.expression)
            is Result.Error -> showError(result.error)
        }
    }

    private fun showExpression(expression: Expression) {
        root.apply {
            expressionTv.text = expression.value
            translationTv.text = expression.translation
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, "Error! ${error.message}", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun new(expressionId: Long) = FragExpressionDetails(expressionId)
    }
}
