package br.com.levez.challenge.delivery.ui.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

private const val ANIMATION_DURATION_IN_MILLIS = 300L

fun View.crossFadeAnimation(viewForVisible: View) {
    viewForVisible.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(ANIMATION_DURATION_IN_MILLIS)
            .setListener(null)
    }
    animate()
        .alpha(0f)
        .setDuration(ANIMATION_DURATION_IN_MILLIS)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}
