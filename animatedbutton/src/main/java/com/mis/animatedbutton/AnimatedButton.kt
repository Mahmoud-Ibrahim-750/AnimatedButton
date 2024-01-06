package com.mis.animatedbutton

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.mis.animatedbutton.ViewProvider.createFailureImageView
import com.mis.animatedbutton.ViewProvider.createProgressBar
import com.mis.animatedbutton.ViewProvider.createSuccessImageView
import com.mis.animatedbutton.ViewProvider.createTextView


/**
 * AnimatedButton is a custom view that represents a button with various states and animations.
 * It includes features such as loading indicator, success, and failure states with corresponding animations.
 *
 * @param context The context in which the button is created.
 * @param attrs The AttributeSet containing custom attributes for the button.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource defining default values for the view.
 */
class AnimatedButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /**
     * Button states.
     */
    enum class ButtonState {
        NORMAL,
        LOADING,
        SUCCESS,
        FAILURE
    }

    /**
     * Button transition animation duration.
     */
    private var animationDuration = DEFAULT_TRANSITION_ANIMATION_DURATION

    /**
     * Button fade animation duration.
     */
    private var fadeAnimationDuration = DEFAULT_FADE_ANIMATION_DURATION

    /**
     * Button current state.
     */
    private var currentState = ButtonState.NORMAL
    val state: ButtonState get() = currentState


    /**
     * Button animator responsible for handling animations.
     */
    private var buttonAnimator: ButtonAnimator

    /**
     * Button click listener.
     */
    private var onClickListener: OnClickListener? = null

    /**
     * Auto-loading when clicked (default true).
     */
    private var autoLoading = DEFAULT_AUTO_LOADING


    /**
     * Button views with an immutable instance exposed for for accessing from the button instance..
     */
    private val _textView: TextView
    val textView get() = _textView

    private val _progressBar: ProgressBar
    val progressBar get() = _progressBar

    private val _successImageView: ImageView
    val successImageView get() = _successImageView

    private val _failureImageView: ImageView
    val failureImageView get() = _failureImageView

    /**
     * Initialize the AnimatedButton.
     */
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
        _textView = createTextView(context, attrs)
        _progressBar = createProgressBar(context, attrs)
        _successImageView = createSuccessImageView(context, attrs)
        _failureImageView = createFailureImageView(context, attrs)

        addView(_textView)
        addView(_progressBar)
        addView(_successImageView)
        addView(_failureImageView)

        // set other attributes (e.g., animationDuration)
        setOtherAttributes(attrs)

        buttonAnimator = ButtonAnimator(animationDuration, fadeAnimationDuration)
        this.setOnClickListener(this)
    }

    /**
     * Sets other attributes for the button based on the provided AttributeSet.
     *
     * @param attrs The AttributeSet containing custom attributes for the button.
     */
    private fun setOtherAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton)
        typedArray.apply {
            animationDuration =
                getInteger(R.styleable.AnimatedButton_animationDuration, animationDuration)
            fadeAnimationDuration = animationDuration / 2
            autoLoading = getBoolean(R.styleable.AnimatedButton_autoLoading, autoLoading)
        }

        typedArray.recycle()
    }


    /**
     * Sets the animation duration properties for the button.
     *
     * @param duration The duration for button animations.
     * @return The AnimatedButton instance.
     */
    fun setAnimationDuration(duration: Int): AnimatedButton {
        animationDuration = duration
        fadeAnimationDuration = duration / 2
        return this
    }

    /**
     * Sets the animation duration properties for the button.
     *
     * @param autoLoading The value to be set.
     * @return The AnimatedButton instance.
     */
    fun setAutoLoading(autoLoading: Boolean): AnimatedButton {
        this.autoLoading = autoLoading
        return this
    }


    /**
     * A customizable click listener interface. Overrides the click behavior to apply a unique
     * behavior for the AnimatedButton
     */
    fun interface OnClickListener {
        /**
         * Called when the button is clicked.
         *
         * @param view The view that was clicked.
         */
        fun onClick(view: View)
    }

    /**
     * Sets the click listener for the button.
     *
     * @param listener The custom click listener to be set.
     */
    fun setOnClickListener(listener: OnClickListener): AnimatedButton {
        onClickListener = listener
        return this
    }

    /**
     * Handles the click event for the button.
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View) {
        if (!autoLoading) {
            onClickListener?.onClick(view)
            return
        }

        when (currentState) {
            ButtonState.NORMAL -> showLoading()

            // un-reachable cases by default as the button gets disabled
            ButtonState.LOADING -> {}
            ButtonState.SUCCESS -> {}
            ButtonState.FAILURE -> {}
        }

        onClickListener?.onClick(view)
    }


    private var _isAnimating = false
    val isAnimating get() = _isAnimating

    private var nextAnimation: (() -> Unit)? = null

    /**
     * Initiates the animation to transition the button from the loading state to the normal state.
     */
    private fun animateLoadingToNormalState() {
        _isAnimating = true
        buttonAnimator.buttonExpand(this)
        buttonAnimator.viewFadeIn(_progressBar) {
            buttonAnimator.viewFadeOut(_textView) {
                _isAnimating = false
                currentState = ButtonState.NORMAL
                nextAnimation?.invoke()
                this.isClickable = true
            }
        }
    }

    /**
     * Initiates the animation to transition the button from the normal state to the loading state.
     */
    private fun animateNormalToLoadingState() {
        _isAnimating = true
        this.isClickable = false
        currentState = ButtonState.LOADING
        buttonAnimator.buttonShrink(this)
        buttonAnimator.viewFadeIn(_textView) {
            buttonAnimator.viewFadeOut(_progressBar) {
                _isAnimating = false
                nextAnimation?.invoke()
            }
        }
    }

    /**
     * Initiates the animation to transition the button from the done state to the normal state.
     */
    private fun animateSuccessToNormalState() {
        _isAnimating = true
        buttonAnimator.buttonExpand(this)
        buttonAnimator.viewFadeIn(_successImageView) {
            buttonAnimator.viewFadeOut(_textView) {
                _isAnimating = false
                currentState = ButtonState.NORMAL
                nextAnimation?.invoke()
                this.isClickable = true
            }
        }
    }

    /**
     * Initiates the animation to transition the button from the loading state to the done state.
     */
    private fun animateLoadingToSuccessState() {
        _isAnimating = true
        this.isClickable = false
        buttonAnimator.viewFadeIn(_progressBar)
        buttonAnimator.viewFadeOut(_successImageView) {
            _isAnimating = false
            currentState = ButtonState.SUCCESS
            nextAnimation?.invoke()
        }
    }

    /**
     * Initiates the animation to transition the button from the error state to the normal state.
     */
    private fun animateFailureToNormalState() {
        _isAnimating = true
        buttonAnimator.buttonExpand(this)
        buttonAnimator.viewFadeIn(_failureImageView) {
            buttonAnimator.viewFadeOut(_textView) {
                _isAnimating = false
                currentState = ButtonState.NORMAL
                nextAnimation?.invoke()
                this.isClickable = true
            }
        }
    }

    /**
     * Initiates the animation to transition the button from the loading state to the error state.
     */
    private fun animateLoadingToFailureState() {
        _isAnimating = true
        this.isClickable = false
        buttonAnimator.viewFadeIn(_progressBar)
        buttonAnimator.viewFadeOut(_failureImageView) {
            _isAnimating = false
            currentState = ButtonState.FAILURE
            nextAnimation?.invoke()
        }
    }

    fun showLoading() {
        if (currentState != ButtonState.NORMAL) return

        if (isAnimating) nextAnimation = {
            animateNormalToLoadingState()
            nextAnimation = null
        }
        else animateNormalToLoadingState()
    }

    fun showSuccess() {
        if (currentState != ButtonState.LOADING) return

        if (isAnimating) nextAnimation = {
            animateLoadingToSuccessState()
            nextAnimation = null
        }
        else animateLoadingToSuccessState()
    }

    fun showFailure() {
        if (currentState != ButtonState.LOADING) return

        if (isAnimating) nextAnimation = {
            animateLoadingToFailureState()
            nextAnimation = null
        }
        else animateLoadingToFailureState()
    }

    fun showNormal() {
        when (currentState) {
            ButtonState.NORMAL -> {}
            ButtonState.LOADING -> {
                if (isAnimating) nextAnimation = {
                    animateLoadingToNormalState()
                    nextAnimation = null
                }
                else animateLoadingToNormalState()
            }

            ButtonState.SUCCESS -> {
                if (isAnimating) nextAnimation = {
                    animateSuccessToNormalState()
                    nextAnimation = null
                }
                else animateSuccessToNormalState()
            }

            ButtonState.FAILURE -> {
                if (isAnimating) nextAnimation = {
                    animateFailureToNormalState()
                    nextAnimation = null
                }
                else animateFailureToNormalState()
            }
        }
    }
}
