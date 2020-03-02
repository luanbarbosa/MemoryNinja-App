package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.add.FragExpressionNew
import com.luanbarbosagomes.hmr.feature.list.FragExpressions
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.launch

class FragMain : Fragment() {

    private val parentActivity: ActivityMain
        get() = activity as ActivityMain

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with (inflater.inflate(R.layout.fragment_main, container, false)) {

            addBtn.setOnClickListener {
                parentActivity.showScreen(FragExpressionNew.new)
            }
            listBtn.setOnClickListener {
                parentActivity.showScreen(FragExpressions.new)
            }
            clearDbBtn.setOnClickListener {
                // TODO - temporary code
                lifecycleScope.launch {
                    ExpressionRepository(database).deleteAll()
                }
            }
            return this
        }
    }

    companion object {
        val new = FragMain()
    }
}
