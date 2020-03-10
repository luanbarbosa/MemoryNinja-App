package com.luanbarbosagomes.hmr.feature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.add.NewExpressionViewModel.State
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_new_expression.*
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragNewExpression : BaseMainFragment() {

    private val model by viewModels<NewExpressionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_new_expression, container, false)

        model
            .status
            .observeForever { status ->
                when (status) {
                    State.Success -> {
                        "Saved!".toastIt()
                        clearFields()
                    }
                    is State.Error -> {
                        "Something went wrong :(".toastIt(short = true)
                    }
                }
            }

        root.saveBtn.setOnClickListener {
            model.saveExpression(
                expressionEt.text.toString(),
                translationEt.text.toString()
            )
        }

        return root
    }

    private fun clearFields() {
        expressionEt.text.clear()
        translationEt.text.clear()
    }

    companion object {

        val new = FragNewExpression()
    }
}
