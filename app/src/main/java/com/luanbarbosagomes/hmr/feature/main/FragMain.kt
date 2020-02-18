package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.add.FragNewExpression
import com.luanbarbosagomes.hmr.feature.add.NewExpressionStatus.FAILED
import com.luanbarbosagomes.hmr.feature.add.NewExpressionStatus.SAVED
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_new_expression.*
import kotlinx.android.synthetic.main.fragment_new_expression.view.*

class FragMain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        root.addBtn.setOnClickListener {
            (activity as ActivityMain).showScreen(FragNewExpression.new)
        }
        return root
    }

    companion object {
        val new = FragMain()
    }
}
