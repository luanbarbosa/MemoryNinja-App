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
import com.luanbarbosagomes.hmr.feature.add.FragExpressionNew
import com.luanbarbosagomes.hmr.feature.list.FragExpressions
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.launch

class FragMain : BaseMainFragment() {

    private val authModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with (inflater.inflate(R.layout.fragment_main, container, false)) {

            addBtn.setOnClickListener {
                mainActivity.showScreen(FragExpressionNew.new)
            }
            listBtn.setOnClickListener {
                mainActivity.showScreen(FragExpressions.new)
            }
            clearDbBtn.setOnClickListener {
                // TODO - temporary code
                lifecycleScope.launch {
                    ExpressionRepository(database).deleteAll()
                }
            }
            logoutBtn.setOnClickListener {
                authModel.logout()
                mainActivity.restart()
            }
            return this
        }
    }

    companion object {
        val new = FragMain()
    }
}
