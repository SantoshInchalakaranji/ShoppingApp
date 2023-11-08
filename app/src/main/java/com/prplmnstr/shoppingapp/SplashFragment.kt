package com.prplmnstr.shoppingapp

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        val lottieAnimationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)



        // Set a listener to navigate to the next screen when the animation finishes
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {

                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)


            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }
        })

        return view
    }


}