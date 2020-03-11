package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.details.ExpressionViewModel
import com.luanbarbosagomes.hmr.utils.toastIt
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.launch

class FragMain : BaseMainFragment() {

    private val mainSharedModel by activityViewModels<MainViewModel>()

    private val expressionViewModel by viewModels<ExpressionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false).also { setupUi(it) }
    }

    private fun setupUi(rootView: View) {
        with (rootView) {
            addBtn.setOnClickListener { mainSharedModel.addExpression() }
            listBtn.setOnClickListener { mainSharedModel.listExpressions() }
            logoutBtn.setOnClickListener { mainSharedModel.logout() }
            randomBtn.setOnClickListener {
                lifecycleScope.launch {
                    val exp = expressionViewModel.expressionRepository.remoteRepository.getRandom()
                    "${exp ?: "NOT FOUND!"}".toastIt()
                }
            }

            // TODO - temporary code ----------------------------
            clearDbBtn.setOnClickListener { expressionViewModel.deleteAll() }
            // TODO - temporary code ----------------------------
        }
    }

    companion object {
        val new = FragMain()
    }
}
