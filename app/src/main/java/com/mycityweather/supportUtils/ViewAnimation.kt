package com.mycityweather.supportUtils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

class ViewAnimation {

    fun expand(v: View, animListener: AnimListener) {
        val a = expandAction(v)
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                animListener.onFinish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        v.startAnimation(a)
    }

    fun expand(v: View) {
        val a = expandAction(v)
        v.startAnimation(a)
    }

    private fun expandAction(v: View): Animation {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight
        v.layoutParams.height = 0
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setDuration(
            (targetHeight / v.context.resources.displayMetrics.density) as Long
        )
        v.startAnimation(a)
        return a
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setDuration(
            (initialHeight / v.context.resources
                .displayMetrics.density) as Long
        )
        v.startAnimation(a)
    }

    interface AnimListener {
        fun onFinish()
    }


}