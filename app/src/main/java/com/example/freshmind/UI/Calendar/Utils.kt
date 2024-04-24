package com.example.freshmind.UI.Calendar.Utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.freshmind.UI.Calendar.StatusBarColorLifecycleObserver

/**
 * Utility functions for Views
 */
fun View.makeVisible() { // Makes view visible
    visibility = View.VISIBLE
}

fun View.makeInVisible() { // Makes view invisible
    visibility = View.INVISIBLE
}

fun View.makeGone() { // Makes view gone
    visibility = View.GONE
}

internal fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color) // Returns color from resources

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color)) // Sets text color from resources

fun Fragment.addStatusBarColorUpdate(@ColorRes colorRes: Int) {
    view?.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(
        StatusBarColorLifecycleObserver(
            requireActivity(),
            requireContext().getColorCompat(colorRes),
        ),
    )
}
