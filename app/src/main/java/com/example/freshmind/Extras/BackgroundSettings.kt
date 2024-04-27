package com.example.freshmind.Extras

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
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
        "light" -> R.color.lightGray
        "dark" -> R.color.darkerGray
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
 * This function returns the color resource ID for the background color based on the global theme
 * @param context The context of the activity
 * @return The color resource ID for the background color
 */
fun getSideBarColor(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.color.softGreen
        "dark" -> R.color.salmon
        "midnight" -> R.color.chalkyGold
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.color.gold
        }
    }
}

/**
 * This function returns the drawable resource ID for the background color based on the global theme
 * @param context The context of the activity
 * @return The drawable resource ID for the background color
 */
fun getDrawableResource(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.drawable.side_nav_bar_light
        "dark" -> R.drawable.side_nav_bar_dark
        "midnight" -> R.drawable.side_nav_bar
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.drawable.side_nav_bar
        }
    }
}

fun changeSpinner(context: Context, spinner : Spinner) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.chalkyGold
        "light" -> R.color.softGreen
        "dark" -> R.color.darkGray
        else -> R.color.black // Change this to the default text color
    }

    val textColor = when (globalTheme) {
        "midnight" -> R.color.black
        "light" -> R.color.comet
        "dark" -> R.color.white
        else -> R.color.black // Change this to the default text color
    }
    val selectedItemTextView = spinner.selectedView as? TextView
    selectedItemTextView?.setTextColor(context.resources.getColor(textColor))
    selectedItemTextView?.setBackgroundColor(context.resources.getColor(backgroundColor))
}
fun changeSpinnerTextBox(context: Context, text : TextView) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.chalkyGold
        "light" -> R.color.softGreen
        "dark" -> R.color.darkGray
        else -> R.color.black // Change this to the default text color
    }

    val textColor = when (globalTheme) {
        "midnight" -> R.color.black
        "light" -> R.color.comet
        "dark" -> R.color.white
        else -> R.color.black // Change this to the default text color
    }
    text.setTextColor(context.resources.getColor(textColor))
    text.setBackgroundColor(context.resources.getColor(backgroundColor))
}

/**
 * This function changes the background color of the given views based on the global theme
 * @param context The context of the activity
 * @param views The views to change the background color of
 */
fun changeTextColors(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.white
        "light" -> R.color.comet
        "dark" -> R.color.white
        else -> R.color.black // Change this to the default text color
    }

    textViews.forEach { it.setTextColor(context.resources.getColor(textColor)) }
}

fun getCalendarTodaybg(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.drawable.light_calendar_today_bg
        "dark" -> R.drawable.dark_calendar_today_bg
        "midnight" -> R.drawable.calendar_today_bg
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.drawable.calendar_today_bg
        }
    }
}

fun getCalendarSelectedbg(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.drawable.light_calendar_selected_bg
        "dark" -> R.drawable.dark_calendar_selected_bg
        "midnight" -> R.drawable.calendar_selected_bg
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.drawable.calendar_selected_bg
        }
    }
}

fun getToolbarColor(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.color.lightLavender
        "dark" -> R.color.purple_200
        "midnight" -> R.color.deepPurple
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.color.deepPurple
        }
    }
}

fun getButtonColor(context: Context): Int {
    return when (globalTheme) {
        "light" -> R.color.softGreen
        "dark" -> R.color.salmon
        "midnight" -> R.color.chalkyGold
        // Add more color mappings as needed
        else -> {
            Log.e("BackgroundSettings", "Unknown color name: $globalTheme")
            throw IllegalArgumentException("Unknown color name: $globalTheme")
            R.color.chalkyGold
        }
    }
}
fun changeButtonColor(context: Context, vararg textViews: TextView) {
    val textColor = when (globalTheme) {
        "midnight" -> R.color.black
        "light" -> R.color.white
        "dark" -> R.color.black
        else -> R.color.black // Change this to the default text color
    }
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.chalkyGold
        "light" -> R.color.softGreen
        "dark" -> R.color.salmon
        else -> R.color.black // Change this to the default text color
    }
    textViews.forEach { it.setTextColor(context.resources.getColor(textColor))
        it.setBackgroundColor(context.resources.getColor(backgroundColor))}
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
        "light" -> R.color.black
        "dark" -> R.color.white
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
        "light" -> R.color.softGreen
        "dark" -> R.color.salmon
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
        "light" -> R.color.black
        "dark" -> R.color.white
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
        "light" -> R.color.amethystSmoke
        "dark" -> R.color.lightGray
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
        "light" -> R.color.amethystSmoke
        "dark" -> R.color.lightGray
        else -> R.color.black // Change this to the default text color
    }
    textViews.forEach { it.setBackgroundColor(context.resources.getColor(backgroundColor)) }
}

fun changeTextAdapter(context: Context, vararg textViews: TextView) {
    val backgroundColor = when (globalTheme) {
        "midnight" -> R.color.lightGray
        "light" -> R.color.darkerGray
        "dark" -> R.color.lightGray
        else -> R.color.black // Change this to the default text color
    }
    textViews.forEach { it.setTextColor(context.resources.getColor(backgroundColor)) }
}
