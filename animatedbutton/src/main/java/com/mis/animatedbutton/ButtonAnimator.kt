package com.mis.animatedbutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import androidx.core.animation.doOnEnd


class ButtonAnimator(
    private var buttonAnimationDuration: Int,
    private val viewsAnimationDuration: Int
) {

    private var _isRunning = false

    private var widthBeforeShrink = -100
    private val fadeInAlpha = 0f
    private val fadeOutAlpha = 1f


    /**
     * Fade in a view. There is a listener to indicate the end of animation.
     *
     * @param view The view to be faded in
     * @param laterAction What to do after this animation ends
     */
    fun viewFadeIn(view: View, laterAction: (() -> Unit)? = null) {
        _isRunning = true
        view.animate()
            .alpha(fadeInAlpha)
            .setDuration(viewsAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    _isRunning = false
                    laterAction?.invoke()
                }
            })
            .start()
    }

    /**
     * Fade out a view.
     *
     * @param view The view to be faded out
     * @param laterAction What to do after this animation ends
     */
    fun viewFadeOut(view: View, laterAction: (() -> Unit)? = null) {
        _isRunning = true
        view.animate()
            .alpha(fadeOutAlpha)
            .setDuration(viewsAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    _isRunning = false
                    laterAction?.invoke()
                }
            })
            .start()
    }

    /**
     * Shrink a view.
     *
     * @param view The view to be faded in
     */
    fun buttonShrink(view: View, laterAction: (() -> Unit)? = null) {
        _isRunning = true
        widthBeforeShrink = view.measuredWidth
        val anim = ValueAnimator.ofInt(view.measuredWidth, view.measuredHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = value
            view.requestLayout()
        }
        anim.doOnEnd {
            _isRunning = false
            laterAction?.invoke()
        }
        anim.duration = buttonAnimationDuration.toLong()
        anim.start()
    }

    /**
     * Stretch a view.
     *
     * @param view The view to be expanded
     */
    fun buttonExpand(view: View, laterAction: (() -> Unit)? = null) {
        _isRunning = true
        val anim = ValueAnimator.ofInt(view.measuredWidth, widthBeforeShrink)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = value
            view.requestLayout()
        }
        anim.doOnEnd {
            _isRunning = false
            laterAction?.invoke()
        }
        anim.duration = buttonAnimationDuration.toLong()
        anim.start()
    }
}
