package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.utils.NotificationUtils

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO - Move state change to the view model based on this info?
        intent.getStringExtra(NotificationUtils.ExpressionFromNotification)?.let {
//            showScreen(FragExpressionDetails.new(it))
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_navigation).navigateUp()

}
