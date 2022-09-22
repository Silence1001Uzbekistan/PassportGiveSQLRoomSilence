package com.example.passportgivesqlroomsilence

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.passportgivesqlroomsilence.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.btnAllPeople.setOnClickListener {

            findNavController().navigate(R.id.listFragment)

        }

        binding.btnGivePassport.setOnClickListener {

            findNavController().navigate(R.id.givePassportFragment)

        }

        return binding.root

    }


}