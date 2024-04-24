package com.example.freshmind.Extras

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.freshmind.R
import com.example.freshmind.UI.globalTheme

/**
 * This function returns the color resource ID for the background color based on the global theme
 * @param context The context of the activity
 * @return The color resource ID for the background color
 */
fun getColorResource(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.color.white
        "dark" -> R.color.black
        "midnight" -> R.color.backgroundIndigo
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.color.backgroundIndigo
    } }
    globalTheme = "midnight"
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeTextColors(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

/**
 * This function changes the background color of the given Textviews based on the global theme
 * @param context The context of the activity
 * @param Textviews The views to change the background color of
 */
fun changeTextColorsNT(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.black
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeAdapterTextColors(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeAccountColour(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.gold
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeTitleColor(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor))
        it.setShadowLayer(10F, 2F, 2F, ContextCompat.getColor(context, R.color.gold))}
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeEditBoxColor(context: Context, vararg editTexts: EditText) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.lightGray
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    editTexts.forEach { it.setBackgroundColor(context.resources.getColor(backgroundColor)) }
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeTextBoxColor(context: Context, vararg textViews: TextView) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.lightGray
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setBackgroundColor(context.resources.getColor(backgroundColor)) }
}
