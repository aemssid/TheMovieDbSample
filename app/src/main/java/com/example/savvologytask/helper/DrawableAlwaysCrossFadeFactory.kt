package com.example.savvologytask.helper

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory

class DrawableAlwaysCrossFadeFactory : TransitionFactory<Drawable> {
    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> {
        val resourceTransition: DrawableCrossFadeTransition = DrawableCrossFadeTransition(300, true)
        return resourceTransition
    }

}