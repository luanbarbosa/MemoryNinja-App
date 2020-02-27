package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.details.FragExpressionDetails

class ActivityMain: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showScreen(FragMain.new)
    }

    internal fun showScreen(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.commit {
            if (addToBackStack) addToBackStack(fragment.javaClass.name)
            replace(R.id.mainContainer, fragment)
        }
    }

    internal fun showExpressionDetails(expression: Expression) {
        expression.uid?.let {
            showScreen(FragExpressionDetails.new(it), addToBackStack = true)
        }
    }

}