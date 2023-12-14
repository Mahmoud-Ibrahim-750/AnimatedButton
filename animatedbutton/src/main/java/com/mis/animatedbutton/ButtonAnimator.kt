package com.mis.animatedbutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd


class ButtonAnimator(var animationDuration: Int, private val fadeAnimationDuration: Int) {

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

    fun interface AnimatorListener {
        fun onAnimationEnd(view: View)
    }

    /**
     * Fade in a view. There is a listener to indicate the end of animation.
     *
     * @param view The view to be faded in
     * @param listener The listener to be notified when the animation is complete
     */
    fun viewFadeIn(view: View, exclude: Boolean = false, listener: AnimatorListener? = null) {
        val fadeInAnimation: () -> Unit = {
            isAnimating = true
            view.animate()
                .alpha(fadeInAlpha)
                .setDuration(fadeAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        isAnimating = false
                        listener?.onAnimationEnd(view)
//                        processAnimationQueue()
                        Log.d("ttt", "fade in end ${System.nanoTime()}")
                    }
                })
                .start()
        }

        if (exclude) fadeInAnimation()
//        else if (isAnimating) animationQueue.add(fadeInAnimation)
        else fadeInAnimation()
    }

    /**
     * Fade out a view.
     *
     * @param view The view to be faded out
     * @param listener The listener to be notified when the animation is complete
     */
    fun viewFadeOut(view: View, exclude: Boolean = false, listener: AnimatorListener? = null) {
        val fadeOutAnimation: () -> Unit = {
            isAnimating = true
            view.animate()
                .alpha(fadeOutAlpha)
                .setDuration(fadeAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        isAnimating = false
                        listener?.onAnimationEnd(view)
//                        processAnimationQueue()
                        Log.d("ttt", "fade out end ${System.nanoTime()}")
                    }
                })
                .start()
        }

        if (exclude) fadeOutAnimation()
//        else if (isAnimating) animationQueue.add(fadeOutAnimation)
        else fadeOutAnimation()
    }

    /**
     * Shrink a view.
     *
     * @param view The view to be faded in
     */
    fun viewShrink(view: View, listener: AnimatorListener? = null) {
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
                listener?.onAnimationEnd(view)
//                processAnimationQueue()
                Log.d("ttt", "shrink end ${System.nanoTime()}")
            }
            anim.duration = animationDuration.toLong()
            anim.start()
        }

//        if (isAnimating) animationQueue.add(shrinkAnimation)
//        else shrinkAnimation()
        shrinkAnimation()
    }

    /**
     * Stretch a view.
     *
     * @param view The view to be expanded
     */
    fun viewExpand(view: View, listener: AnimatorListener? = null) {
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
                listener?.onAnimationEnd(view)
//                processAnimationQueue()
                Log.d("ttt", "expand end ${System.nanoTime()}")
            }
            anim.duration = animationDuration.toLong()
            anim.start()
        }

//        if (isAnimating) animationQueue.add(expandAnimation)
//        else expandAnimation()
        expandAnimation()
    }
}
