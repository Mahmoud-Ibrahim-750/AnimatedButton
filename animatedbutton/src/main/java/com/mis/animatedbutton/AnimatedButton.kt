package com.mis.animatedbutton

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import com.mis.animatedbutton.ButtonAnimation.DoneToNormal
import com.mis.animatedbutton.ButtonAnimation.ErrorToNormal
import com.mis.animatedbutton.ButtonAnimation.LoadingToDone
import com.mis.animatedbutton.ButtonAnimation.LoadingToError
import com.mis.animatedbutton.ButtonAnimation.LoadingToNormal
import com.mis.animatedbutton.ButtonAnimation.NormalToLoading
import com.mis.animatedbutton.ButtonAnimation.ResetToNormal
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
        DONE,
        ERROR
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

    // TODO: maybe implement factory pattern to inject this object instead of hilt? does hilt
    //  affect library user (project).
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
     * Button views.
     */
    private val textView: TextView
    private val progressBar: ProgressBar
    private val successImageView: ImageView
    private val failureImageView: ImageView

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
        textView = createTextView(context, attrs)
        progressBar = createProgressBar(context, attrs)
        successImageView = createSuccessImageView(context, attrs)
        failureImageView = createFailureImageView(context, attrs)

        addView(textView)
        addView(progressBar)
        addView(successImageView)
        addView(failureImageView)

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
     * Sets the text of the button.
     *
     * @param newText The new text for the button.
     * @return The AnimatedButton instance.
     */
    fun setText(newText: String): AnimatedButton {
        textView.text = newText
        return this
    }

    /**
     * Sets the indeterminate status of the progress bar.
     *
     * @param indeterminate Whether the progress bar should be indeterminate or not.
     * @return The AnimatedButton instance.
     */
    fun setIndeterminate(indeterminate: Boolean): AnimatedButton {
        progressBar.isIndeterminate = indeterminate
        return this
    }

    /**
     * Sets the progress value of the progress bar.
     *
     * @param progress The progress value.
     * @return The AnimatedButton instance.
     */
    fun setProgress(progress: Int): AnimatedButton {
        progressBar.progress = progress
        return this
    }

    /**
     * Sets the progress value of the progress bar with or without animation.
     *
     * @param progress The progress value.
     * @param animate Whether to animate the progress change.
     * @return The AnimatedButton instance.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun setProgress(progress: Int, animate: Boolean): AnimatedButton {
        progressBar.setProgress(progress, animate)
        return this
    }

    /**
     * Sets the progress indicator color of the progress bar.
     *
     * @param colorRes The color resource for the progress indicator.
     * @return The AnimatedButton instance.
     */
    fun setProgressIndicatorColor(colorRes: Int): AnimatedButton {
        val colorStateList = ColorStateList.valueOf(colorRes)
        progressBar.progressTintList = colorStateList
        return this
    }

    /**
     * Sets the image resource for the success state.
     *
     * @param imageRes The resource ID of the success image.
     * @return The AnimatedButton instance.
     */
    fun setSuccessImageResource(imageRes: Int): AnimatedButton {
        successImageView.setImageResource(imageRes)
        return this
    }

    /**
     * Sets the tint color for the success image.
     *
     * @param colorRes The color resource for tinting the success image.
     * @return The AnimatedButton instance.
     */
    fun setSuccessImageTint(colorRes: Int): AnimatedButton {
        val colorStateList = ColorStateList.valueOf(colorRes)
        successImageView.imageTintList = colorStateList
        return this
    }

    /**
     * Sets the image resource for the failure state.
     *
     * @param imageRes The resource ID of the failure image.
     * @return The AnimatedButton instance.
     */
    fun setFailureImageResource(imageRes: Int): AnimatedButton {
        failureImageView.setImageResource(imageRes)
        return this
    }

    /**
     * Sets the tint color for the failure image.
     *
     * @param colorRes The color resource for tinting the failure image.
     * @return The AnimatedButton instance.
     */
    fun setFailureImageTint(colorRes: Int): AnimatedButton {
        val colorStateList = ColorStateList.valueOf(colorRes)
        failureImageView.imageTintList = colorStateList
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
    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
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
            ButtonState.NORMAL -> {
                animateNormalToLoadingState()
            }

            // un-reachable cases by default as the button gets disabled
            ButtonState.LOADING -> {}
            ButtonState.DONE -> {}
            ButtonState.ERROR -> {}
        }

        onClickListener?.onClick(view)
    }

    /**
     * Checks whether or not the button is currently in an animation.
     *
     * @return `true` if the button is animating, `false` otherwise.
     */
    fun isAnimating() = buttonAnimator.isAnimating()


    // TODO: revisit this function for optimization
    /**
     * Resets the button state, stopping any ongoing animation and returning the button to its normal state.
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
     * Initiates the animation to transition the button from the loading state to the normal state.
     */
    private fun animateLoadingToNormalState() {
        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(progressBar, true)
        buttonAnimator.viewFadeOut(textView)

        this.isClickable = true
        currentState = ButtonState.NORMAL
    }

    /**
     * Initiates the animation to transition the button from the normal state to the loading state.
     */
    private fun animateNormalToLoadingState() {
        buttonAnimator.viewShrink(this)
        buttonAnimator.viewFadeIn(textView, true)
        buttonAnimator.viewFadeOut(progressBar)

        this.isClickable = false
        currentState = ButtonState.LOADING
    }

    /**
     * Initiates the animation to transition the button from the done state to the normal state.
     */
    private fun animateDoneToNormalState() {
        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(successImageView, true)
        buttonAnimator.viewFadeOut(textView)

        this.isClickable = true
        currentState = ButtonState.NORMAL
    }

    /**
     * Initiates the animation to transition the button from the loading state to the done state.
     */
    private fun animateLoadingToDoneState() {
        buttonAnimator.viewFadeIn(progressBar)
        buttonAnimator.viewFadeOut(successImageView)

        this.isClickable = false
        currentState = ButtonState.DONE
    }

    /**
     * Initiates the animation to transition the button from the error state to the normal state.
     */
    private fun animateErrorToNormalState() {
        buttonAnimator.viewExpand(this)
        buttonAnimator.viewFadeIn(failureImageView, true)
        buttonAnimator.viewFadeOut(textView)

        this.isClickable = true
        currentState = ButtonState.NORMAL
    }

    /**
     * Initiates the animation to transition the button from the loading state to the error state.
     */
    private fun animateLoadingToErrorState() {
        buttonAnimator.viewFadeIn(progressBar)
        buttonAnimator.viewFadeOut(failureImageView)

        this.isClickable = false
        currentState = ButtonState.ERROR
    }


    /**
     * Displays a specific animation for the button based on the provided [ButtonAnimation] type.
     *
     * @param animation The type of animation to display.
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
