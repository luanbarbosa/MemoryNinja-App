package com.luanbarbosagomes.hmr.feature.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.details.FragExpressionDetails
import com.luanbarbosagomes.hmr.feature.login.FragLogin
import com.luanbarbosagomes.hmr.utils.NotificationUtils
import com.luanbarbosagomes.hmr.utils.runDelayed

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (App.isLoggedIn)
            showScreen(FragMain.new)
        else
            showScreen(FragLogin.new, addToBackStack = false)

        intent.getLongOrNullExtra(NotificationUtils.ExpressionFromNotification)?.let {
            showScreen(FragExpressionDetails.new(it))
        }
    }

    internal fun showScreen(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.commit(allowStateLoss = true) {
            if (addToBackStack) addToBackStack(fragment.javaClass.name)
            replace(R.id.mainContainer, fragment)
        }
    }

    internal fun showExpressionDetails(expression: Expression) {
        expression.uid?.let {
            showScreen(FragExpressionDetails.new(it), addToBackStack = true)
        }
    }

    fun restart() {
        finish()
        // TODO - re-load activity?
    }

    fun loggedIn() {
        showScreen(FragMain.new)
    }

}

private fun Intent.getLongOrNullExtra(key: String): Long? {
    val value = this.getLongExtra(key, -1L)
    return if (value == -1L) null else value
}