package com.prplmnstr.shoppingapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import com.prplmnstr.shoppingapp.Utility.CategoryFabAnimationUtility
import com.prplmnstr.shoppingapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //To set gradient background to  statusBar and toolbar programmatically.
        val window: Window = requireActivity().window
        window.setBackgroundDrawableResource(R.drawable.toolbar_gradient_bg)
        val customToolbar = binding.customToolbar


        // Set the custom toolbar as the action bar
        (requireActivity() as AppCompatActivity).setSupportActionBar(customToolbar)

        //Hide default title text
        (requireActivity() as AppCompatActivity).getSupportActionBar()
            ?.setDisplayShowTitleEnabled(false);


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupFabClickListener(view)

    }



    private fun setupFabClickListener(view: View) {
        val categoryFabAnimationUtility = CategoryFabAnimationUtility(binding,
            MaterialContainerTransform(),requireContext(),view)
        binding.fab.setOnClickListener {
            if (binding.fab.isExtended) {
               categoryFabAnimationUtility.expandLayoutAndShowBlurEffect()
            } else {
                categoryFabAnimationUtility.collapseLayoutAndHideBlurEffect()
            }
        }

        binding.blurView.setOnClickListener {
            categoryFabAnimationUtility.collapseLayoutAndHideBlurEffect()
        }
    }
}