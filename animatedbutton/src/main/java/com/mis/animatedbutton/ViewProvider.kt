package com.mis.animatedbutton

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.mis.animatedbutton.Utils.getRawDimension

/**
 * ViewProvider is an object that provides methods for creating customized views used in the AnimatedButton library.
 */
object ViewProvider {

    /**
     * Creates a customized TextView for the AnimatedButton.
     *
     * @param context The context in which the TextView is created.
     * @param attrs The AttributeSet containing custom attributes for the TextView.
     * @return A customized TextView.
     */
    fun createTextView(context: Context, attrs: AttributeSet?): TextView {
        val textView = TextView(context).apply {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false
            alpha = 1f

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            typedArray.apply {
                text = getString(R.styleable.AnimatedButton_text) ?: DEFAULT_TEXT
                textSize = getRawDimension(R.styleable.AnimatedButton_textSize, DEFAULT_TEXT_SIZE)
                setTypeface(null, getInt(R.styleable.AnimatedButton_textStyle, DEFAULT_TEXT_STYLE))
                setTextColor(
                    getColor(
                        R.styleable.AnimatedButton_textColor,
                        ConstantsHandler(context).DEFAULT_TEXT_COLOR
                    )
                )
            }

            typedArray.recycle()
        }
        return textView
    }

    /**
     * Creates a customized ProgressBar for the AnimatedButton.
     *
     * @param context The context in which the ProgressBar is created.
     * @param attrs The AttributeSet containing custom attributes for the ProgressBar.
     * @return A customized ProgressBar.
     */
    fun createProgressBar(context: Context, attrs: AttributeSet?): ProgressBar {
        val progressBar = ProgressBar(context).apply {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false
            alpha = 0f

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            val tintList = ColorStateList.valueOf(
                typedArray.getColor(
                    R.styleable.AnimatedButton_indicatorColor,
                    ConstantsHandler(context).DEFAULT_PROGRESS_COLOR
                )
            )
            indeterminateTintList = tintList

            typedArray.recycle()
        }
        return progressBar
    }

    /**
     * Creates a customized ImageView for success state in the AnimatedButton.
     *
     * @param context The context in which the ImageView is created.
     * @param attrs The AttributeSet containing custom attributes for the ImageView.
     * @return A customized ImageView for success state.
     */
    fun createSuccessImageView(context: Context, attrs: AttributeSet?): ImageView {
        val imageView = ImageView(context).apply {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false
            alpha = 0f

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            setImageResource(
                typedArray.getResourceId(
                    R.styleable.AnimatedButton_successIcon,
                    DEFAULT_SUCCESS_ICON
                )
            )
            val tintList = ColorStateList.valueOf(
                typedArray.getColor(
                    R.styleable.AnimatedButton_successIconTint,
                    ConstantsHandler(context).DEFAULT_PROGRESS_COLOR
                )
            )
            imageTintList = tintList

            typedArray.recycle()
        }
        return imageView
    }

    /**
     * Creates a customized ImageView for failure state in the AnimatedButton.
     *
     * @param context The context in which the ImageView is created.
     * @param attrs The AttributeSet containing custom attributes for the ImageView.
     * @return A customized ImageView for failure state.
     */
    fun createFailureImageView(context: Context, attrs: AttributeSet?): ImageView {
        val imageView = ImageView(context).apply {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false
            alpha = 0f

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            setImageResource(
                typedArray.getResourceId(
                    R.styleable.AnimatedButton_failureIcon,
                    DEFAULT_FAILURE_ICON
                )
            )
            val tintList = ColorStateList.valueOf(
                typedArray.getColor(
                    R.styleable.AnimatedButton_failureIconTint,
                    ConstantsHandler(context).DEFAULT_PROGRESS_COLOR
                )
            )
            imageTintList = tintList

            typedArray.recycle()
        }
        return imageView
    }
}
