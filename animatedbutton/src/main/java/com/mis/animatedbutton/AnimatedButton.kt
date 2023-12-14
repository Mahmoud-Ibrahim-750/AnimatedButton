package com.mis.animatedbutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.mis.animatedbutton.ButtonAnimation.DoneToNormal
import com.mis.animatedbutton.ButtonAnimation.ErrorToNormal
import com.mis.animatedbutton.ButtonAnimation.LoadingToDone
import com.mis.animatedbutton.ButtonAnimation.LoadingToError
import com.mis.animatedbutton.ButtonAnimation.LoadingToNormal
import com.mis.animatedbutton.ButtonAnimation.NormalToLoading
import com.mis.animatedbutton.ButtonAnimation.ResetToNormal
import com.mis.animatedbutton.Utils.getRawDimension


class AnimatedButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /*
     * Default colors.
     */
    private val defaultProgressColor = ContextCompat.getColor(context, R.color.white)
    private var defaultTextColor = ContextCompat.getColor(context, R.color.white)

    /*
     * Other default values.
     */
    private val defaultTextSize = 18f
    private val defaultTextStyle = Typeface.NORMAL
    private val defaultAnimationDuration = 500
    private val defaultFadeAnimationDuration = defaultAnimationDuration / 2
    private val defaultText = "Button"
    private val defaultSuccessIconRes = R.drawable.ic_default_done
    private val defaultErrorIconRes = R.drawable.ic_default_error
    private val defaultAutoLoadingBehavior = true

    /**
     * Button states.
     */
    enum class ButtonState {
        NORMAL,
        LOADING,
        DONE,
        ERROR
    }

    /**
     * Button animation transition duration.
     */
    private var animationDuration = defaultAnimationDuration

    /**
     * Button fade animation duration.
     */
    private var fadeAnimationDuration = defaultFadeAnimationDuration

    /**
     * Button state.
     */
    private var currentState = ButtonState.NORMAL

    // TODO: maybe implement factory pattern to inject this object instead of hilt? does hilt
    //  affect library user (project).
    /**
     *
     */
    private var buttonAnimator: ButtonAnimator

    /**
     * Button click listener.
     */
    private var onClickListener: OnClickListener? = null

    /**
     * Auto-loading behavior when clicked indicator (default true).
     */
    private var autoLoading = defaultAutoLoadingBehavior


    /**
     * Button views.
     */
    private val textView: TextView
    private val progressBar: ProgressBar
    private val successImageView: ImageView
    private val failureImageView: ImageView

    init {
        // Initialize button
        isClickable = true
        isFocusable = true
        setPadding(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                resources.displayMetrics
            ).toInt()
        )

        // Initialize and add child views
        textView = createTextView(attrs)
        progressBar = createProgressBar(attrs)
        successImageView = createSuccessImageView(attrs)
        failureImageView = createFailureImageView(attrs)

        addView(textView)
        addView(progressBar)
        addView(successImageView)
        addView(failureImageView)

        // set other attributes (e.g., animationDuration)
        setOtherAttributes(attrs)

        buttonAnimator = ButtonAnimator(animationDuration, fadeAnimationDuration)
        this.setOnClickListener(this)

        // Set initial visibility based on the default state (NORMAL)
        updateVisibility()
    }

    private fun setOtherAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
        typedArray.apply {
            animationDuration =
                getInteger(R.styleable.AnimatedButton_animationDuration, defaultAnimationDuration)
            autoLoading =
                getBoolean(R.styleable.AnimatedButton_autoLoading, defaultAutoLoadingBehavior)
        }

        typedArray.recycle()
    }


    private fun createTextView(attrs: AttributeSet?): TextView {
        val textView = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(CENTER_IN_PARENT, TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)

            text = typedArray.getString(R.styleable.AnimatedButton_text) ?: defaultText
            textSize =
                typedArray.getRawDimension(R.styleable.AnimatedButton_textSize, defaultTextSize)
            setTypeface(
                null,
                typedArray.getInt(R.styleable.AnimatedButton_textStyle, defaultTextStyle)
            )
            setTextColor(
                typedArray.getColor(
                    R.styleable.AnimatedButton_textColor,
                    defaultTextColor
                )
            )

            typedArray.recycle()
        }
        return textView
    }

    private fun createProgressBar(attrs: AttributeSet?): ProgressBar {
        val progressBar = ProgressBar(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(CENTER_IN_PARENT, TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            val tintList = ColorStateList.valueOf(
                typedArray.getColor(R.styleable.AnimatedButton_indicatorColor, defaultProgressColor)
            )
            indeterminateTintList = tintList

            typedArray.recycle()
        }
        return progressBar
    }

    private fun createSuccessImageView(attrs: AttributeSet?): ImageView {
        val imageView = ImageView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(CENTER_IN_PARENT, TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            setImageResource(
                typedArray.getResourceId(
                    R.styleable.AnimatedButton_successIcon,
                    defaultSuccessIconRes
                )
            )
            val tintList = ColorStateList.valueOf(
                typedArray.getColor(
                    R.styleable.AnimatedButton_successIconTint,
                    defaultProgressColor
                )
            )
            imageTintList = tintList

            typedArray.recycle()
        }
        return imageView
    }


    private fun createFailureImageView(attrs: AttributeSet?): ImageView {
        val imageView = ImageView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(CENTER_IN_PARENT, TRUE)
            layoutParams = params
            isFocusable = false
            isClickable = false

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
            setImageResource(
                typedArray.getResourceId(
                    R.styleable.AnimatedButton_failureIcon,
                    defaultErrorIconRes
                )
            )
            val tintList = ColorStateList.valueOf(
                typedArray.getColor(
                    R.styleable.AnimatedButton_failureIconTint,
                    defaultProgressColor
                )
            )
            imageTintList = tintList

            typedArray.recycle()
        }
        return imageView
    }

    // Method to update visibility based on the current state
    private fun updateVisibility() {
        // Implement logic to set visibility for each view based on the currentState
        textView.alpha = 1f
        progressBar.alpha = 0f
        successImageView.alpha = 0f
        failureImageView.alpha = 0f
    }

    /**
     * Set the auto-loading behavior indicator.
     *
     * @param autoLoad A boolean value to be set.
     */
    fun setAutoLoading(autoLoad: Boolean): AnimatedButton {
        autoLoading = autoLoad
        return this
    }

    /**
     * A customizable click listener interface. That's different that the default interface
     * that provides the default stated behaviour.
     */
    fun interface OnClickListener {
        fun onClick(view: View)
    }

    /**
     * Set a click listener for the button.
     */
    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    /**
     * The default onClick method.
     */
    override fun onClick(view: View) {
        if (!autoLoading) {
            onClickListener?.onClick(view)
            return
        }

        when (currentState) {
            ButtonState.NORMAL -> {
                animateNormalToLoadingState()
            }

            ButtonState.LOADING -> {
                // un-reachable case by default since the button is disabled while loading
                animateLoadingToNormalState()
            }

            ButtonState.DONE -> {
                // un-reachable case by default since the button is disabled while loading
                // TODO
            }

            ButtonState.ERROR -> {
                // un-reachable case by default since the button is disabled while loading
                // TODO
            }
        }

        onClickListener?.onClick(view)
    }

    /**
     * Checks whether or not the button is in an animation.
     *
     * @return A boolean, true if the button is animating and false otherwise.
     */
    fun isAnimating() = buttonAnimator.isAnimating()


    /**
     * Reset animation to get button back to normal state.
     */
    private fun resetButtonState() {
        val savedDuration = animationDuration
        buttonAnimator.animationDuration = 0

//        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(successImageView)
        buttonAnimator.viewFadeIn(failureImageView)
        buttonAnimator.viewFadeIn(progressBar)

        buttonAnimator.animationDuration = savedDuration

        currentState = ButtonState.NORMAL
    }

    /**
     * Start animation to get back to normal state.
     */
    private fun animateLoadingToNormalState() {
        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(progressBar) {
            buttonAnimator.viewFadeOut(textView)
        }
        this.isClickable = true
        currentState = ButtonState.NORMAL
    }

    /**
     * Start animation to convert button to loading button
     */
    private fun animateNormalToLoadingState() {
        buttonAnimator.viewShrink(this)
        buttonAnimator.viewFadeIn(textView, true) {
            buttonAnimator.viewFadeOut(progressBar, true)
        }
        this.isClickable = false
        currentState = ButtonState.LOADING
    }

    /**
     * Start animation to get back to normal button from done button.
     */
    private fun animateDoneToNormalState() {
        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(successImageView, true) {
            buttonAnimator.viewFadeOut(textView, true)
        }
        this.isClickable = true
        currentState = ButtonState.NORMAL
    }

    /**
     * Start animation to convert button to done view.
     */
    private fun animateLoadingToDoneState() {
        buttonAnimator.viewFadeIn(progressBar) {
            buttonAnimator.viewFadeOut(successImageView)
        }
        this.isClickable = false
        currentState = ButtonState.DONE
    }

    /**
     * Start animation to get back to normal button from error view
     */
    private fun animateErrorToNormalState() {
        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(failureImageView) {
            buttonAnimator.viewFadeOut(textView)
        }
        this.isClickable = true
        currentState = ButtonState.NORMAL
    }

    /**
     * Start animation to convert button to error view
     */
    private fun animateLoadingToErrorState() {
        buttonAnimator.viewFadeIn(progressBar) {
            buttonAnimator.viewFadeOut(failureImageView)
        }
        this.isClickable = false
        currentState = ButtonState.ERROR
    }


    /**
     * The main method for animations.
     *
     * @param animation A specific animation type.
     */
    fun showAnimation(animation: ButtonAnimation) {
        when (animation) {
            ResetToNormal -> resetButtonState()
            NormalToLoading -> animateNormalToLoadingState()
            LoadingToNormal -> animateLoadingToNormalState()
            LoadingToDone -> animateLoadingToDoneState()
            LoadingToError -> animateLoadingToErrorState()
            DoneToNormal -> animateDoneToNormalState()
            ErrorToNormal -> animateErrorToNormalState()
        }
    }

}
