package com.mis.animatedbutton

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat

/**
 * Default values for text properties.
 */
const val DEFAULT_TEXT = "Button"
const val DEFAULT_TEXT_SIZE = 18f
const val DEFAULT_TEXT_STYLE = Typeface.NORMAL

/**
 * Default values for animations.
 */
const val DEFAULT_TRANSITION_ANIMATION_DURATION = 500
const val DEFAULT_FADE_ANIMATION_DURATION = DEFAULT_TRANSITION_ANIMATION_DURATION / 2
const val DEFAULT_AUTO_LOADING = true

/**
 * Default values for icons.
 */
val DEFAULT_SUCCESS_ICON = R.drawable.ic_default_success
val DEFAULT_FAILURE_ICON = R.drawable.ic_default_failure

/**
 * ConstantsHandler is a utility class that encapsulates default values for the AnimatedButton library.
 * It provides default text, text size, text style, animation durations, icons, and colors.
 *
 * @param context The context used to retrieve resources for default values.
 */
class ConstantsHandler(context: Context) {

    /**
     * Default colors retrieved from resources.
     */
    val DEFAULT_PROGRESS_COLOR by lazy { ContextCompat.getColor(context, R.color.white) }
    val DEFAULT_TEXT_COLOR by lazy { ContextCompat.getColor(context, R.color.white) }

}
