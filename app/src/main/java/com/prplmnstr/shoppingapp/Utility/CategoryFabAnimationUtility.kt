package com.prplmnstr.shoppingapp.Utility

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.databinding.FragmentHomeBinding

class CategoryFabAnimationUtility(private val binding: FragmentHomeBinding,
                                  private val transform: MaterialContainerTransform = MaterialContainerTransform(),
                                    private val context: Context,
                                        private val view: View) {




    /**
     * Initializes the click listener for the Floating Action Button (FAB).
     * Calls the corresponding functions for expanding or collapsing the layout
     * and showing or hiding the blur effect.
     */


    /**
     * Expands the layout, shows the blur effect, and modifies the FAB properties.
     * Applies the MaterialContainerTransform for the layout expansion animation.
     */
     fun expandLayoutAndShowBlurEffect() {
        binding.fab.shrink()
        fadeInBlurEffect()
        showHiddenLayout()
        modifyFabPropertiesOnExpand()
        applyTransformForExpansion()
    }

    /**
     * Collapses the layout, hides the blur effect, and resets the FAB properties.
     * Applies the MaterialContainerTransform for the layout collapse animation.
     */
     fun collapseLayoutAndHideBlurEffect() {
        resetFabPropertiesOnCollapse()
        fadeOutBlurEffect()
        hideBlurViewAndHiddenLayout()
        applyTransformForCollapse()
    }

    /**
     * Fades in the blur effect and makes the blur view visible.
     */
    private fun fadeInBlurEffect() {
        binding.blurView.visibility = View.VISIBLE
        val fadeInAnimator = ObjectAnimator.ofFloat(binding.blurView, "alpha", 0f, 1f)
        fadeInAnimator.duration = 800
        fadeInAnimator.start()
    }

    /**
     * Fades out the blur effect and hides the blur view after the animation is complete.
     */
    private fun fadeOutBlurEffect() {
        val fadeOutAnimator = ObjectAnimator.ofFloat(binding.blurView, "alpha", 1f, 0f)
        fadeOutAnimator.duration = 800
        fadeOutAnimator.start()
        fadeOutAnimator.doOnEnd {
            binding.blurView.visibility = View.GONE
        }
    }

    private fun showHiddenLayout() {
        binding.hiddenLayout.visibility = View.VISIBLE
    }

    /**
     * Modifies the FAB properties when expanding the layout.
     */
    private fun modifyFabPropertiesOnExpand() {
        binding.fab.setIconResource(R.drawable.close)
        binding.fab.backgroundTintList = ContextCompat.getColorStateList(context, R.color.orange)
    }

    /**
     * Resets the FAB properties when collapsing the layout.
     */
    private fun resetFabPropertiesOnCollapse() {
        binding.fab.backgroundTintList = ContextCompat.getColorStateList(context, R.color.black)
        binding.fab.setIconResource(R.drawable.category)
    }

    private fun hideBlurViewAndHiddenLayout() {
        binding.fab.postDelayed({
            binding.fab.extend()
            binding.hiddenLayout.visibility = View.GONE
        }, 800)
    }

    /**
     * Applies the MaterialContainerTransform for expanding the layout.
     */
    private fun applyTransformForExpansion() {
        transform.startView = binding.fab
        transform.endView = binding.hiddenLayout
        transform.duration = 800
        transform.scrimColor = context.getColor(R.color.transparent)
        transform.setAllContainerColors(context.getColor(android.R.color.black))
        TransitionManager.beginDelayedTransition(view as ViewGroup, transform)
    }

    /**
     * Applies the MaterialContainerTransform for collapsing the layout.
     */
    private fun applyTransformForCollapse() {
        transform.startView = binding.hiddenLayout
        transform.endView = binding.fab
        TransitionManager.beginDelayedTransition(view as ViewGroup, transform)
    }

}