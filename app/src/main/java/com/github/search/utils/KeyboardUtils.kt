package com.github.search.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardUtils {
    companion object {
        @JvmStatic
        fun hideKeyboard(activity: androidx.core.app.ComponentActivity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                activity.findViewById<View>(android.R.id.content)?.windowToken,
                0
            )
        }
    }
}