package com.example.passportgivesqlroomsilence

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.passportgivesqlroomsilence.Database.AppDatabase
import com.example.passportgivesqlroomsilence.Models.Citizens
import com.example.passportgivesqlroomsilence.databinding.FragmentGivePassportBinding
import com.example.passportgivesqlroomsilence.databinding.ItemDiaolgTasdiqlashBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlin.random.Random

class GivePassportFragment : Fragment() {

    lateinit var binding: FragmentGivePassportBinding
    lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGivePassportBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageBackAdd.setOnClickListener {

            findNavController().popBackStack()

        }

        binding.imageAdd.setOnClickListener {


            Dexter.withContext(requireContext())
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        Toast.makeText(
                            requireContext(),
                            "Rasmlarga kirishga ruxsat berildi",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {

                        Toast.makeText(
                            requireContext(),
                            "Rasmlarga kirishga ruxsat bermadinggiz ",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()

                    }
                }).check()

        }

        appDatabase = AppDatabase.getInstance(requireContext())
        var listCitizens = appDatabase.citizenDao().getAllCitizens()
        var listSeriya = ArrayList<String>()

        for (citizen in listCitizens) {
            listSeriya.add(citizen.passportSeriya!!)
        }


        val citizens = Citizens()

        binding.studentDateEt.setOnClickListener {

            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { datePicker, i, i2, i3 ->

                binding.studentDateEt.text = "$i3.${i2 + 1}.$i"
                citizens.passportOlganVaqti = binding.studentDateEt.text.trim().toString()
                binding.edtPassportUddati.text = "$i3.${i2 + 1}.${i + 10}"
                citizens.passportDedline = binding.edtPassportUddati.text.trim().toString()

            }

            datePickerDialog.show()

        }

        binding.btnSave.setOnClickListener {

            citizens.name = binding.edtManName.text.trim().toString()
            citizens.lastName = binding.edtManSoname.text.trim().toString()
            citizens.otasiningIsmi = binding.edtManO.text.trim().toString()
            citizens.city = binding.edtShaharTuman.text.trim().toString()


            //citizens.imagePath = ab
            citizens.viloyat = binding.spinnerViloyat.selectedItemPosition
            citizens.jinsi = binding.spinnerJinsi.selectedItemPosition
            citizens.uyManzili = binding.edtUyManzili.text.trim().toString()


            citizens.passportSeriya = seriyaBer(listSeriya)


            if (citizens.name != "" && citizens.lastName != "") {

                val alertDialog = AlertDialog.Builder(requireContext(), R.style.NewDialog)
                val itemDialog =
                    ItemDiaolgTasdiqlashBinding.inflate(LayoutInflater.from(requireContext()))
                val dialog = alertDialog.create()
                dialog.setView(itemDialog.root)

                itemDialog.btnNo.setOnClickListener {

                    dialog.cancel()

                }

                itemDialog.btnYes.setOnClickListener {

                    appDatabase.citizenDao().addCitizens(citizens)
                    val id =
                        appDatabase.citizenDao().getCitizenById(citizens.passportSeriya!!)
                    Toast.makeText(
                        requireContext(),
                        "${id} ${citizens.name} qo'shildi",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.cancel()
                    findNavController().popBackStack()

                }

                dialog.show()

            } else {

                Toast.makeText(context, "Avval ma'lumotlarni to'ldiring...", Toast.LENGTH_SHORT)
                    .show()

            }

        }

    }

    //passport seriya tayyorlash bir xil bo'lmagan
    fun seriyaBer(listSeriya: List<String>): String {

        var seriya = ""
        var a = Random.nextInt(25)
        var b = Random.nextInt(25)
        var q = 0

        for (x in 'A'..'Z') {

            if (q == a) {
                seriya += x
            }

            if (q == b) {

                seriya += x

            }

            q++

        }

        for (i in 0..6) {

            seriya += Random.nextInt(10)

        }

        for (s in listSeriya) {

            if (s == seriya) {

                seriyaBer(listSeriya)
                break

            }

        }

        return seriya

    }


}