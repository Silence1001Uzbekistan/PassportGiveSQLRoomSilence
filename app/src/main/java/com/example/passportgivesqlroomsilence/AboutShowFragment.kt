package com.example.passportgivesqlroomsilence

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.passportgivesqlroomsilence.Models.Citizens
import com.example.passportgivesqlroomsilence.databinding.FragmentAboutShowBinding

class AboutShowFragment : Fragment() {

    lateinit var binding: FragmentAboutShowBinding
    lateinit var citizens: Citizens

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAboutShowBinding.inflate(LayoutInflater.from(requireContext()))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageBackAbout.setOnClickListener {

            findNavController().popBackStack()

        }

        citizens = arguments?.getSerializable("keyCitizen") as Citizens

        //binding.imageShow.setImageURI(Uri.parse(citizens.imagePath))

        if (citizens.jinsi == 0) {
            binding.txtShowJinsi.text = "Erkak"
        } else {
            binding.txtShowJinsi.text = "Ayol"
        }

        when (citizens.viloyat) {


            0 -> binding.txtShowViloyat.text = "Toshkent"
            1 -> binding.txtShowViloyat.text = "Andijon"
            2 -> binding.txtShowViloyat.text = "Farg'ona"
            3 -> binding.txtShowViloyat.text = "Namangan"
            4 -> binding.txtShowViloyat.text = "Jizzax"
            5 -> binding.txtShowViloyat.text = "Sirdaryo"
            6 -> binding.txtShowViloyat.text = "Navoiy"
            7 -> binding.txtShowViloyat.text = "Surxandaruo"
            8 -> binding.txtShowViloyat.text = "Qashqadaryo"
            9 -> binding.txtShowViloyat.text = "Samarqand"
            10 -> binding.txtShowViloyat.text = "Buxoro"
            11 -> binding.txtShowViloyat.text = "Xorazm"
            12 -> binding.txtShowViloyat.text = "Qoraqalpog'iston Respublikasi"

        }

        binding.txtShowName.text = "${citizens.name} ${citizens.lastName}"
        binding.txtShowPassportOlganVaqti.text = citizens.passportOlganVaqti
        binding.txtShowShaharTuman.text = citizens.city
        binding.txtShowUyningManzili.text = citizens.uyManzili

    }


}