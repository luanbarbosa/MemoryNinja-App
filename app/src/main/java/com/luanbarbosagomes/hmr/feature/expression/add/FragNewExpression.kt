package com.luanbarbosagomes.hmr.feature.expression.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.expression.add.NewExpressionViewModel.State
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_new_expression.*
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragNewExpression : BaseMainFragment() {

    private val viewModel by viewModels<NewExpressionViewModel>()

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

    private fun setupUi() {
        rootView.saveBtn.setOnClickListener {
            viewModel.saveExpression(
                expressionEt.text.toString(),
                translationEt.text.toString()
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
            State.Success -> {
                "Saved!".toastIt()
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

    private fun clearFields() {
        expressionEt.text.clear()
        translationEt.text.clear()
        expressionEt.requestFocus()
    }

}
