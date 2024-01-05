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

    private val animationQueue: MutableList<() -> Unit> = mutableListOf()
    private var isAnimating = false

    private var widthBeforeShrink = -100
    private val fadeInAlpha = 0f
    private val fadeOutAlpha = 1f

    fun isAnimating(): Boolean = isAnimating

    private fun processAnimationQueue() {
        if (animationQueue.isNotEmpty() && !isAnimating) {
            val nextAnimation = animationQueue.removeAt(0)
            nextAnimation()
        }
    }

    /**
     * Fade in a view. There is a listener to indicate the end of animation.
     *
     * @param view The view to be faded in
     * @param exclude A flag indicating whether or not this animation should wait for the previous
     * one to complete first. True means it should not wait, false means it should wait.
     */
    fun viewFadeIn(view: View, exclude: Boolean = false) {
        val fadeInAnimation: () -> Unit = {
            isAnimating = true
            view.animate()
                .alpha(fadeInAlpha)
                .setDuration(viewsAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        isAnimating = false
                        processAnimationQueue()
                    }
                })
                .start()
        }

        if (exclude) fadeInAnimation()
        else if (isAnimating) animationQueue.add(fadeInAnimation)
        else fadeInAnimation()
    }

    /**
     * Fade out a view.
     *
     * @param view The view to be faded out
     * @param exclude A flag indicating whether or not this animation should wait for the previous
     * one to complete first. True means it should not wait, false means it should wait.
     */
    fun viewFadeOut(view: View, exclude: Boolean = false) {
        val fadeOutAnimation: () -> Unit = {
            isAnimating = true
            view.animate()
                .alpha(fadeOutAlpha)
                .setDuration(viewsAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        isAnimating = false
                        processAnimationQueue()
                    }
                })
                .start()
        }

        if (exclude) fadeOutAnimation()
        else if (isAnimating) animationQueue.add(fadeOutAnimation)
        else fadeOutAnimation()
    }

    /**
     * Shrink a view.
     *
     * @param view The view to be faded in
     */
    fun buttonShrink(view: View) {
        val shrinkAnimation: () -> Unit = {
            isAnimating = true
            widthBeforeShrink = view.measuredWidth
            val anim = ValueAnimator.ofInt(view.measuredWidth, view.measuredHeight)
            anim.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.width = value
                view.requestLayout()
            }
            anim.doOnEnd {
                isAnimating = false
                processAnimationQueue()
            }
            anim.duration = buttonAnimationDuration.toLong()
            anim.start()
        }

        if (isAnimating) animationQueue.add(shrinkAnimation)
        else shrinkAnimation()
    }

    /**
     * Stretch a view.
     *
     * @param view The view to be expanded
     */
    fun buttonExpand(view: View) {
        val expandAnimation: () -> Unit = {
            isAnimating = true
            val anim = ValueAnimator.ofInt(view.measuredWidth, widthBeforeShrink)
            anim.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.width = value
                view.requestLayout()
            }
            anim.doOnEnd {
                isAnimating = false
                processAnimationQueue()
            }
            anim.duration = buttonAnimationDuration.toLong()
            anim.start()
        }

        if (isAnimating) animationQueue.add(expandAnimation)
        else expandAnimation()
    }
}
