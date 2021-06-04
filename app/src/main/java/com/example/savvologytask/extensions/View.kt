package com.example.savvologytask.extensions

import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator

fun View.setAnimatedClickListener(onClickTask : (View) -> Unit) {
    this
        .animate()
        .scaleX(1.05f)
        .scaleY(1.05f)
        .setDuration(75)
        .setInterpolator(AccelerateInterpolator())
        .withEndAction {
            this.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(75)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction {
                    onClickTask(this)
                }
        }

}