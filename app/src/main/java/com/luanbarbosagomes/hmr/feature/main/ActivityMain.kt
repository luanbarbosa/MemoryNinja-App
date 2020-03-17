package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.add.FragNewExpression
import com.luanbarbosagomes.hmr.feature.details.FragExpressionDetails
import com.luanbarbosagomes.hmr.feature.list.FragListExpressions
import com.luanbarbosagomes.hmr.feature.login.FragLogin
import com.luanbarbosagomes.hmr.feature.preference.FragStorageOption
import com.luanbarbosagomes.hmr.feature.main.MainViewModel.State
import com.luanbarbosagomes.hmr.feature.main.MainViewModel.State.*
import com.luanbarbosagomes.hmr.utils.NotificationUtils
import com.luanbarbosagomes.hmr.utils.toastIt

class ActivityMain : AppCompatActivity() {

    private val mainModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO - Move state change to the view model based on this info?
        intent.getStringExtra(NotificationUtils.ExpressionFromNotification)?.let {
            showScreen(FragExpressionDetails.new(it))
        }

        subscribeToData()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_navigation).navigateUp()

    private fun subscribeToData() {
        mainModel.state.observe(
            this,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            LoginNeeded -> showScreen(FragLogin.new, addToBackStack = false)
            LoggedIn -> showScreen(FragMain.new)
            StorageOptionNeeded -> showScreen(FragStorageOption.new)
            NewExpression -> showScreen(FragNewExpression.new)
            ListExpressions -> showScreen(FragListExpressions.new)
            is DetailExpression -> showExpressionDetails(state.expression)
            is Error -> {
                "Something went wrong!".toastIt()
                // TODO - display an error view
            }
        }
    }

    private fun showScreen(fragment: Fragment, addToBackStack: Boolean = true) {
//        supportFragmentManager.commit(allowStateLoss = true) {
//            if (addToBackStack) addToBackStack(fragment.javaClass.name)
//            replace(R.id.mainContainer, fragment)
//        }
    }

    private fun showExpressionDetails(expression: Expression) {
        showScreen(FragExpressionDetails.new(expression.uid), addToBackStack = true)
    }

}
