package com.example.passportgivesqlroomsilence

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.passportgivesqlroomsilence.Database.AppDatabase
import com.example.passportgivesqlroomsilence.Models.Citizens
import com.example.passportgivesqlroomsilence.databinding.FragmentEditBinding
import com.example.passportgivesqlroomsilence.databinding.FragmentGivePassportBinding
import com.example.passportgivesqlroomsilence.databinding.ItemDiaolgTasdiqlashBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : Fragment() {

    lateinit var binding: FragmentEditBinding
    lateinit var appDatabase: AppDatabase
    lateinit var citizens: Citizens

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditBinding.inflate(layoutInflater, container, false)

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

        citizens = arguments?.getSerializable("citizensKey") as Citizens

        binding.edtManName.setText(citizens.name)
        binding.edtManSoname.setText(citizens.lastName)
        binding.edtManO.setText(citizens.otasiningIsmi)
        binding.edtShaharTuman.setText(citizens.city)
        binding.studentDateEt.setText(citizens.passportOlganVaqti)
        binding.edtPassportUddati.setText(citizens.passportDedline)

/*        absolutePath = citizens.imagePath!!
        binding.imageAdd.setImageURI(Uri.parse(citizens.imagePath))*/

        binding.spinnerViloyat.setSelection(citizens.viloyat!!)
        binding.spinnerJinsi.setSelection(citizens.jinsi!!)
        binding.edtUyManzili.setText(citizens.uyManzili)


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

            citizens.name = binding.edtManName.text.toString().trim()
            citizens.lastName = binding.edtManSoname.text.toString().trim()
            citizens.otasiningIsmi = binding.edtManO.text.toString().trim()
            citizens.city = binding.edtShaharTuman.text.toString().trim()
            citizens.passportOlganVaqti = binding.studentDateEt.text.toString().trim()
            citizens.passportDedline = binding.edtPassportUddati.text.toString().trim()
            //citizens.imagePath = absolutePath
            citizens.viloyat = binding.spinnerViloyat.selectedItemPosition
            citizens.jinsi = binding.spinnerJinsi.selectedItemPosition

            if (citizens.name != "" && citizens.lastName != "") {
                val alertDialog = AlertDialog.Builder(context, R.style.NewDialog)
                val itemDialog = ItemDiaolgTasdiqlashBinding.inflate(LayoutInflater.from(context))
                val dialog = alertDialog.create()
                dialog.setView(itemDialog.root)
                itemDialog.btnNo.setOnClickListener { dialog.cancel() }
                itemDialog.btnYes.setOnClickListener {
                    var id = appDatabase.citizenDao().getCitizenById(citizens.passportSeriya!!)
                    citizens.id = id
                    appDatabase.citizenDao().updateCitizen(citizens)
                    Toast.makeText(
                        context,
                        "${id} ${citizens.name} tahrirlandi",
                        Toast.LENGTH_SHORT
                    )
                        .show()
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
//
//    var absolutePath = ""
//    private val getImageContent =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri ->
//            uri ?: return@registerForActivityResult
//            binding.imageAdd.setImageURI(uri)
//            val inputStream = activity?.contentResolver?.openInputStream(uri)
//            val format = SimpleDateFormat("yyMMdd_hhss").format(Date())
//            val file = File(activity?.filesDir, "${format}image.jpg")
//            val fileOutputStream = FileOutputStream(file)
//            inputStream?.copyTo(fileOutputStream)
//            inputStream?.close()
//            fileOutputStream.close()
//            absolutePath = file.absolutePath
//
//            Toast.makeText(context, "$absolutePath", Toast.LENGTH_SHORT).show()
//        }

}