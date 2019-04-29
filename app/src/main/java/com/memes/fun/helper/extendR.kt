package com.memes.`fun`.helper

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

fun <T : View> Activity.find(@IdRes id: Int, clickListener: ((view: View) -> Unit)? = null): T {
    val res = findViewById<T>(id)
    clickListener?.let { res.setOnClickListener(it) }

    return res
}