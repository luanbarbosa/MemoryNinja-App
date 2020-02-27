package com.luanbarbosagomes.hmr.feature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.SaveStatus
import kotlinx.android.synthetic.main.fragment_new_expression.*
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragNewExpression : Fragment() {

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
                    SaveStatus.SAVED -> {
                        Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show()
                        clearFields()
                    }
                    SaveStatus.FAILED -> {
                        Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_LONG).show()
                    }
                    else -> {
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
