package com.prplmnstr.shoppingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.prplmnstr.shoppingapp.databinding.FragmentFavoriteBinding
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel


class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.text.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
        }

    }
}