package com.example.freshmind.Extras

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.freshmind.R
import com.example.freshmind.UI.globalTheme

fun getColorResource(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.color.white
        "dark" -> R.color.black
        "midnight" -> R.color.backgroundIndigo
        // Add more color mappings as needed
        else -> throw IllegalArgumentException("Unknown color name: $globalTheme")
    }
}

fun changeTextColors(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

fun changeAdapterTextColors(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

fun changeAccountColour(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.gold
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

fun changeTitleColor(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor))
        it.setShadowLayer(10F, 2F, 2F, ContextCompat.getColor(context, R.color.gold))}
}

fun changeEditBoxColor(context: Context, vararg editTexts: EditText) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.lightGray
        else -> R.color.black // Change this to the default text color
    }

    editTexts.forEach { it.setBackgroundColor(context.resources.getColor(backgroundColor)) }
}

fun changeTextBoxColor(context: Context, vararg textViews: TextView) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.lightGray
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setBackgroundColor(context.resources.getColor(backgroundColor)) }
}