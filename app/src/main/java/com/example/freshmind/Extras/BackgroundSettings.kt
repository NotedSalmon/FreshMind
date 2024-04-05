package com.example.freshmind.Extras

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.freshmind.R
import com.example.freshmind.UI.globalTheme

fun getColorResource(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.color.white
        "dark" -> R.color.black
        "midnightSushi" -> R.color.backgroundIndigo
        // Add more color mappings as needed
        else -> throw IllegalArgumentException("Unknown color name: $globalTheme")
    }
}

fun changeTextColors(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnightSushi" -> android.R.color.white
        else -> android.R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}