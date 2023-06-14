package com.github.search.utils

import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.hideKeyboard() {
    KeyboardUtils.hideKeyboard(requireActivity())
    clearFocus()
}

fun Fragment.clearFocus() {
    val view = activity?.currentFocus
    if (view is EditText) {
        view.clearFocus()
    }
}