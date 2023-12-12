package com.mis.animatedbutton

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.mis.animatedbutton.Utils.getColor
import com.mis.animatedbutton.Utils.getRawDimension
import com.mis.animatedbutton.Utils.setColorFilter


class AnimatedButton : RelativeLayout, View.OnClickListener {

    /*
     * Default colors.
     */
    private val defaultProgressColor = resources.getColor(R.color.colorAccent, context)
    private var defaultButtonColor = resources.getColor(R.color.colorPrimary, context)
    private var defaultIconTintColor = resources.getColor(R.color.colorAccent, context)
    private var defaultTextColor = resources.getColor(R.color.white, context)

    /*
     * Other default values.
     */
    private val defaultTextSize = 18f
    private val defaultTextStyle = Typeface.NORMAL
    private val defaultFullyExpandedButtonWidth = 1500
    private val defaultAnimationDuration = 500
    private val defaultFadeAnimationDuration = defaultAnimationDuration / 2
    private val defaultText = "Button"
    private val defaultBackgroundResVal = -1

    /**
     * Button states.
     */
    enum class ButtonState {
        NORMAL,
        LOADING,
        DONE,
        ERROR
    }

//    private val TAG = AnimatedButton::class.java.simpleName

    /**
     * Button width when fully expanded.
     */
    private var buttonExpandedWidth = defaultFullyExpandedButtonWidth

    /**
     * Button background color.
     */
    private var buttonBackgroundColor = defaultButtonColor

    /**
     * Done icon tint color.
     */
    private var doneIconTintColor = defaultIconTintColor

    /**
     * Error icon tint color.
     */
    private var errorIconTintColor = defaultIconTintColor

    /**
     * Button Background resource.
     */
    private var buttonBackgroundRes = defaultBackgroundResVal

    /**
     * Button progress indicator color.
     */
    private var progressIndicatorColor = defaultProgressColor

    /**
     * Button text value
     */
    private var buttonText: String = defaultText

    /**
     * Button text size.
     */
    private var buttonTextSize = defaultTextSize

    /**
     * Button text style.
     */
    private var buttonTextStyle = defaultTextStyle

    /**
     * Button text color.
     */
    private var buttonTextColor = defaultTextColor

    /**
     * Button error icon.
     */
    private var errorDrawable: Drawable? = null

    /**
     * Button done icon.
     */
    private var doneDrawable: Drawable? = null

//    /**
//     * A flag to determine whether or not to change button state from error to normal
//     * automatically after some seconds.
//     */
//    private var autoErrorToNormalTransition = false

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
    private var buttonState = ButtonState.NORMAL

    // TODO: maybe implement factory pattern to inject this object instead of hilt? does hilt
    //  affect library user (project).
    /**
     *
     */
    private lateinit var buttonAnimator: ButtonAnimator

    /**
     * Button click listener.
     */
    private var onClickListener: OnClickListener? = null


    // TODO: implement the builder pattern here
    /**
     * Button views.
     */
    private lateinit var buttonLayout: RelativeLayout
    private lateinit var buttonTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var doneLayout: LinearLayout
    private lateinit var doneImageView: ImageView
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImageView: ImageView


    /*
     * Constructors
     */
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        retrieveAndSetAttributes(attrs, context)
        resetButtonState()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    /**
     * Initialize class instance (inflate layout, init views and other objects.
     */
    private fun init() {
        val view =
            LayoutInflater.from(context).inflate(R.layout.default_animated_button_layout, this)
        buttonLayout = view.findViewById(R.id.button_layout)
        buttonTextView = view.findViewById(R.id.button_label_tv)
        progressBar = view.findViewById(R.id.progress_bar)
        doneLayout = view.findViewById(R.id.done_layout)
        doneImageView = view.findViewById(R.id.done_image_view)
        errorLayout = view.findViewById(R.id.error_layout)
        errorImageView = view.findViewById(R.id.error_image_view)

        // set the default click listener add the default behaviour of the button
        // however the custom click listener can be set or not (null)
        buttonLayout.setOnClickListener(this)
        buttonAnimator =
            ButtonAnimator(buttonExpandedWidth, animationDuration, fadeAnimationDuration)
    }

    /**
     * Set attributes from xml.
     *
     * @param attrs An AttributeSet instance containing the attributes to be set
     * @param context A context of where the button is initialized
     */
    private fun retrieveAndSetAttributes(attrs: AttributeSet, context: Context) {
        val styledAttrTypedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.AnimatedButton, 0, 0
        )

        retrieveAttributes(styledAttrTypedArray)
        setRetrievedAttributes()

        styledAttrTypedArray.recycle()
    }

    /**
     * Retrieve XML-passed attributes.
     *
     * @param styledAttrTypedArray The array containing attribute values.
     */
    private fun retrieveAttributes(styledAttrTypedArray: TypedArray) {
        styledAttrTypedArray.apply {
            buttonText = getString(R.styleable.AnimatedButton_text) ?: defaultText
            buttonTextSize = getRawDimension(R.styleable.AnimatedButton_textSize, defaultTextSize)
            buttonTextStyle = getInt(R.styleable.AnimatedButton_textStyle, defaultTextStyle)
            buttonTextColor = getColor(R.styleable.AnimatedButton_textColor, defaultTextColor)

            animationDuration =
                getInteger(R.styleable.AnimatedButton_animationDuration, defaultAnimationDuration)

            buttonBackgroundColor =
                getColor(R.styleable.AnimatedButton_backgroundColor, defaultButtonColor)

            buttonBackgroundRes =
                getResourceId(
                    R.styleable.AnimatedButton_backgroundResource,
                    defaultBackgroundResVal
                )

            // TODO: Until escaping the pipe symbol is done, this will remain commented
//            // Get the background attribute value
//            getValue(R.styleable.AnimatedButton_background, backgroundValue)
//
//            // Check the type of the attribute value
//            if (backgroundValue.type == TypedValue.TYPE_REFERENCE) {
//                // It's a resource (drawable, color, etc.)
//                val resourceId = backgroundValue.resourceId
//                // Set the resource
//                buttonLayout.setBackgroundResource(resourceId)
//            } else if (backgroundValue.type == TypedValue.TYPE_INT_COLOR_ARGB8
//                || backgroundValue.type == TypedValue.TYPE_INT_COLOR_ARGB4
//                || backgroundValue.type == TypedValue.TYPE_INT_COLOR_RGB8
//                || backgroundValue.type == TypedValue.TYPE_INT_COLOR_RGB4
//            ) {
//                // It's a color
//                val colorValue = backgroundValue.data
//                // Set the color
//                buttonLayout.setBackgroundColor(colorValue)
//            }

            doneIconTintColor =
                getColor(R.styleable.AnimatedButton_doneIconTintColor, defaultIconTintColor)

            errorIconTintColor =
                getColor(R.styleable.AnimatedButton_errorIconTintColor, defaultIconTintColor)

            progressIndicatorColor =
                getColor(R.styleable.AnimatedButton_indicatorColor, defaultProgressColor)

            errorDrawable = getDrawable(R.styleable.AnimatedButton_errorIcon)
            doneDrawable = getDrawable(R.styleable.AnimatedButton_doneIcon)
//            autoErrorToNormalTransition =
//                getBoolean(R.styleable.AnimatedButton_returnToNormalAfterError, true)
        }
    }

    /**
     * Set the retrieved attribute values to the corresponding variable of the button instance
     */
    private fun setRetrievedAttributes() {
        buttonTextView.text = buttonText
        buttonTextView.textSize = buttonTextSize
        buttonTextView.setTypeface(null, buttonTextStyle)
        buttonTextView.setTextColor(buttonTextColor)

        if (buttonBackgroundRes != defaultBackgroundResVal) {
            buttonLayout.setBackgroundResource(buttonBackgroundRes)
        }
        if (buttonBackgroundColor != defaultButtonColor) {
            buttonLayout.setBackgroundColor(buttonBackgroundColor)
        }

        val doneTintList = ColorStateList.valueOf(doneIconTintColor)
        doneImageView.imageTintList = doneTintList

        val errorTintList = ColorStateList.valueOf(errorIconTintColor)
        errorImageView.imageTintList = errorTintList

        // TODO: use setters for other attributes if possible
        setProgressIndicatorColor(progressIndicatorColor)

        errorDrawable?.let { errorImageView.setImageDrawable(it) }
        doneDrawable?.let { doneImageView.setImageDrawable(it) }
    }

    /**
     * Set button text.
     *
     * @param string The string to be set.
     * @return
     */
    fun setText(string: String?): AnimatedButton {
        buttonTextView.text = string
        return this
    }

    /**
     * Set button animation duration.
     *
     * @param animDuration The time to be set.
     * @return
     */
    fun setAnimationDuration(animDuration: Int, fadeAnimDuration: Int): AnimatedButton {
        buttonAnimator = ButtonAnimator(buttonExpandedWidth, animDuration, fadeAnimDuration)
        return this
    }

    /**
     * Set button background color.
     *
     * @param color Color to be set.
     * @return
     */
    fun setButtonBackgroundColor(color: Int): AnimatedButton {
        buttonLayout.setBackgroundColor(color)
        return this
    }

    /**
     * Set button background color.
     *
     * @param resId Resource to be set.
     * @return
     */
    fun setButtonBackgroundResource(resId: Int): AnimatedButton {
        buttonLayout.setBackgroundResource(resId)
        return this
    }

    /**
     * Set progress indicator color.
     *
     * @param color The color to be set.
     */
    fun setProgressIndicatorColor(color: Int): AnimatedButton {
        progressBar.setColorFilter(color)
        return this
    }

    /**
     * Set text size.
     *
     * @param size The size to be set.
     */
    fun setTextSize(size: Float): AnimatedButton {
        buttonTextView.textSize = size
        return this
    }

    /**
     * Set text style.
     *
     * @param typeface The typeface from which a style is retrieved.
     */
    fun setTextStyle(typeface: Typeface): AnimatedButton {
        buttonTextView.typeface = typeface
        return this
    }

    /**
     * Set text color.
     * @param color The color to be set.
     * @return
     */
    fun setTextColor(color: Int): AnimatedButton {
        buttonTextView.setTextColor(resources.getColor(color, context))
        return this
    }

    /**
     * Set progress indicator error icon.
     * @param errorIcon The drawable to be set or a null to remove it.
     * @return
     */
    fun setErrorIcon(errorIcon: Drawable?): AnimatedButton {
        if (errorIcon != null) errorLayout.background = errorIcon
        return this
    }

    /**
     * Set progress indicator done icon.
     * @param doneIcon The drawable to be set.
     * @return
     */
    fun setDoneIcon(doneIcon: Drawable?): AnimatedButton {
        if (doneIcon != null) doneLayout.background = doneIcon
        return this
    }

    /**
     * Retrieve the button state.
     */
    fun getState(): ButtonState {
        return buttonState
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
     * The default click behaviour.
     */
    override fun onClick(view: View) {
        if (view.id != R.id.button_layout) return

        when (buttonState) {
            ButtonState.NORMAL -> {
                animateNormalToLoadingState()
                buttonState = ButtonState.LOADING
            }

            ButtonState.LOADING -> {
                // un-reachable case by default since the button is disabled while loading
                animateLoadingToNormalState()
                buttonState = ButtonState.NORMAL
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
     * Reset animation to get button back to normal state.
     */
    fun resetButtonState() {
//        buttonLayout.isVisible = true
//        doneLayout.isVisible = true
//        errorLayout.isVisible = true

        val savedDuration = animationDuration
        buttonAnimator.animationDuration = 0

        buttonAnimator.viewExpand(buttonLayout)
        buttonAnimator.viewFadeIn(doneLayout)
        buttonAnimator.viewFadeIn(errorLayout)
        buttonAnimator.viewFadeIn(progressBar)

        buttonAnimator.animationDuration = savedDuration

        buttonState = ButtonState.NORMAL
    }

    /**
     * Start animation to get back to normal state.
     */
    fun animateLoadingToNormalState() {
        buttonAnimator.viewExpand(buttonLayout)
        buttonAnimator.viewFadeIn(progressBar) {
            buttonAnimator.viewFadeOut(buttonTextView)
        }
        buttonLayout.isClickable = true
        buttonState = ButtonState.NORMAL
    }

    /**
     * Start animation to convert button to loading button
     */
    fun animateNormalToLoadingState() {
        buttonAnimator.viewShrink(buttonLayout)
        buttonAnimator.viewFadeIn(buttonTextView) {
            buttonAnimator.viewFadeOut(progressBar)
        }
        buttonLayout.isClickable = false
        buttonState = ButtonState.LOADING
    }

    /**
     * Start animation to get back to normal button from done button.
     */
    fun animateDoneToNormalState() {
        buttonAnimator.viewExpand(buttonLayout)
        buttonAnimator.viewFadeIn(doneLayout) {
            buttonAnimator.viewFadeOut(buttonTextView)
        }
        buttonLayout.isClickable = true
        buttonState = ButtonState.NORMAL
    }

    /**
     * Start animation to convert button to done view.
     */
    fun animateLoadingToDoneState() {
        buttonAnimator.viewFadeIn(progressBar) {
            buttonAnimator.viewFadeOut(doneLayout)
        }
        buttonLayout.isClickable = false
        buttonState = ButtonState.DONE
    }

    /**
     * Start animation to get back to normal button from error view
     */
    fun animateErrorToNormalState() {
        buttonAnimator.viewExpand(buttonLayout)
        buttonAnimator.viewFadeIn(errorLayout) {
            buttonAnimator.viewFadeOut(buttonTextView)
        }
        buttonLayout.isClickable = true
        buttonState = ButtonState.NORMAL
    }

    /**
     * Start animation to convert button to error view
     */
    fun animateLoadingToErrorState() {
        buttonAnimator.viewFadeIn(progressBar) {
            buttonAnimator.viewFadeOut(errorLayout)
        }
        buttonLayout.isClickable = false
        buttonState = ButtonState.ERROR
    }
}
