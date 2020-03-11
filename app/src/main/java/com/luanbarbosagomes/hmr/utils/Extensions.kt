package com.luanbarbosagomes.hmr.utils

import android.os.Handler
import android.view.View
import android.widget.Toast
import com.luanbarbosagomes.hmr.App

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun String.toastIt(short: Boolean = false) = Toast.makeText(
    App.appContext,
    this,
    if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
).show()

fun <R> (() -> R).runDelayed(delay: Long) = Handler().postDelayed(
    { this.invoke() },
    delay
)

fun <T> ignoreError(function: () -> T?): T? {
    return try {
        function.invoke()
    } catch (ignoredError: Throwable) {
        null
    }
}