package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.launch

class FragMain : BaseMainFragment() {

    private val mainModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false).also { setupUi(it) }
    }

    private fun setupUi(rootView: View) {
        with (rootView) {
            addBtn.setOnClickListener { mainModel.addExpression() }
            listBtn.setOnClickListener { mainModel.listExpressions() }
            logoutBtn.setOnClickListener { mainModel.logout() }

            // TODO - temporary code ----------------------------
            clearDbBtn.setOnClickListener {
                lifecycleScope.launch {
                    ExpressionRepository(database).deleteAll()
                }
            }
            // TODO - temporary code ----------------------------
        }
    }

    companion object {
        val new = FragMain()
    }
}
