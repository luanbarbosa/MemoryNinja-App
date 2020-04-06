package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.utils.toastIt

class ActivityMain : AppCompatActivity() {

    private var settingsCallback: (() -> Unit)? = null
    private var filterCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_navigation).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottomappbar_menu, menu)
        return true
    }

    fun setupToolbar(toolbar: Toolbar, settingsCallback: () -> Unit, filterCallback: () -> Unit) {
        this.settingsCallback = settingsCallback
        this.filterCallback = filterCallback
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_settings -> settingsCallback?.invoke()
            R.id.app_bar_filter -> filterCallback?.invoke()
        }
        return true
    }
}
