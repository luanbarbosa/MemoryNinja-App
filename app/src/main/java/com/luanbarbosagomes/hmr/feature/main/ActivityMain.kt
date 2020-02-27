package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.utils.NotificationUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityMain: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showScreen(FragMain.new)


        GlobalScope.launch {
            ExpressionRepository(App.database).getRandom()?.let {
                NotificationUtils.showExpressionReminderNotification(it)
            }
        }
    }

    internal fun showScreen(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.commit {
            if (addToBackStack) addToBackStack(fragment.javaClass.name)
            replace(R.id.mainContainer, fragment)
        }
    }

}