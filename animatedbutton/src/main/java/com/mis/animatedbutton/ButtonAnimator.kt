package com.mis.animatedbutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View


class ButtonAnimator(private var buttonExpandedWidth: Int,
                     var animationDuration: Int,
                     private val fadeAnimationDuration: Int) {

    private val fadeInAlpha = 0f
    private val fadeOutAlpha = 1f

    fun interface AnimatorListener {
        fun onAnimationEnd(view: View)
    }

    /**
     * Fade in a view. There is a listener to indicate the end of animation.
     *
     * @param view The view to be faded in
     * @param listener The listener to be notified when the animation is complete
     */
    fun viewFadeIn(view: View, listener: AnimatorListener? = null) {
        view.animate()
            .alpha(fadeInAlpha)
            .setDuration(fadeAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    listener?.onAnimationEnd(view)
                }
            })
            .start()
    }

    /**
     * Fade out a view.
     *
     * @param view The view to be faded out
     * @param listener The listener to be notified when the animation is complete
     */
    fun viewFadeOut(view: View, listener: AnimatorListener? = null) {
        view.animate()
            .alpha(fadeOutAlpha)
            .setDuration(fadeAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    listener?.onAnimationEnd(view)
                }
            })
            .start()
    }

    /**
     * Shrink a view.
     *
     * @param view The view to be faded in
     */
    fun viewShrink(view: View) {
        val anim = ValueAnimator.ofInt(view.measuredWidth, view.measuredHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = value
            view.requestLayout()
        }
        anim.duration = animationDuration.toLong()
        anim.start()
    }

    /**
     * Stretch a view.
     *
     * @param view The view to be expanded
     */
    fun viewExpand(view: View) {
        val anim = ValueAnimator.ofInt(view.measuredWidth, buttonExpandedWidth)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = value
            view.requestLayout()
        }
        anim.duration = animationDuration.toLong()
        anim.start()
    }
}
