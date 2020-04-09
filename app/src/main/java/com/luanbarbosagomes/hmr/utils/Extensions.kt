package com.luanbarbosagomes.hmr.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun withDelay(delay : Long, block : () -> Unit) {
    Handler().postDelayed(Runnable(block), delay)
}

fun <T> ignoreError(function: () -> T?): T? {
    return try {
        function.invoke()
    } catch (ignoredError: Throwable) {
        null
    }
}

fun View.closeAndroidKeyboard() =
    context.inputMethodManager.hideSoftInputFromWindow(windowToken , 0)

private val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun Context.hideKeyboard(view: View) {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
}