package com.example.passportgivesqlroomsilence

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.passportgivesqlroomsilence.Adapter.RvAdapter
import com.example.passportgivesqlroomsilence.Adapter.RvOnClick
import com.example.passportgivesqlroomsilence.Database.AppDatabase
import com.example.passportgivesqlroomsilence.Models.Citizens
import com.example.passportgivesqlroomsilence.databinding.FragmentListBinding

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var rvAdapter: RvAdapter
    lateinit var appDatabase: AppDatabase
    lateinit var listData: ArrayList<Citizens>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageBackList.setOnClickListener {

            findNavController().popBackStack()

        }

        binding.imageListSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {

                val listSearch = ArrayList<Citizens>()

                for (citizens in listData) {

                    if (citizens.name?.subSequence(0, p0?.length!!) == p0) {
                        listSearch.add(citizens)
                    }

                }

                search(listSearch)

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val listSearch = ArrayList<Citizens>()

                Toast.makeText(requireContext(), "$p0", Toast.LENGTH_SHORT).show()

                if (p0!!.length == 3) {

                    binding.imageListSearch.onActionViewCollapsed()
                    binding.imageListSearch.setQuery("", false)
                    binding.imageListSearch.clearFocus()

                    binding.imageBackList.visibility = View.VISIBLE
                    binding.txtTitle.visibility = View.VISIBLE

                    search(listData)

                    return false

                }

                for (i in 0 until listData.size) {


                    if (listData[i].name!!.subSequence(0, 1).toString()
                            .equals(p0.toString(), ignoreCase = true)
                    ) {

                        listSearch.add(listData[i])

                    }

                }

                search(listSearch)

                return true

            }


        })


        binding.imageListSearch.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {


                binding.imageBackList.visibility = View.VISIBLE
                binding.txtTitle.visibility = View.VISIBLE

                search(listData)

                return false

            }

        })


        binding.imageListSearch.setOnSearchClickListener {

            binding.imageListSearch.clearFocus()
            binding.imageBackList.visibility = View.GONE
            binding.txtTitle.visibility = View.GONE

        }

    }

    fun search(list: List<Citizens>) {
        rvAdapter = RvAdapter(list, object : RvOnClick {
            override fun itemOnClick(citizens: Citizens, position: Int) {

                findNavController().navigate(
                    R.id.aboutShowFragment,
                    bundleOf("keyCitizen" to citizens)
                )

            }

            override fun moreOnClick(citizens: Citizens, position: Int, v: ImageView) {

                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.pop_menu)
                popupMenu.setOnMenuItemClickListener {

                    when (it.itemId) {

                        R.id.delete_menu -> {

                            val dialog = AlertDialog.Builder(context)
                            dialog.setMessage("${citizens.name} ${citizens.passportSeriya} fuqaro o'chirilsinmi?")
                            dialog.setNegativeButton(
                                "Ha"
                            ) { dialog, which ->

                                val id = appDatabase.citizenDao()
                                    .getCitizenById(citizens.passportSeriya!!)
                                citizens.id = id
                                appDatabase.citizenDao().deleteCitizen(citizens)
                                Toast.makeText(
                                    context,
                                    "${citizens.id} deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onResume()

                            }
                            dialog.setPositiveButton("Yo'q") { dialog, which ->
                            }
                            dialog.show()
                        }

                        R.id.edit_menu -> {
                            findNavController().navigate(

                                R.id.editFragment,
                                bundleOf("citizensKey" to citizens)

                            )
                        }

                    }

                    true

                }
                popupMenu.show()
            }

        })

        binding.rvList.adapter = rvAdapter

    }

    override fun onResume() {
        super.onResume()


        appDatabase = AppDatabase.getInstance(requireContext())
        listData = ArrayList()
        listData.addAll(appDatabase.citizenDao().getAllCitizens())

        rvAdapter = RvAdapter(listData, object : RvOnClick {
            override fun itemOnClick(citizens: Citizens, position: Int) {

                findNavController().navigate(
                    R.id.aboutShowFragment,
                    bundleOf("keyCitizen" to citizens)
                )

            }

            override fun moreOnClick(citizens: Citizens, position: Int, v: ImageView) {


                val popupMenu = PopupMenu(requireContext(), v)
                popupMenu.inflate(R.menu.pop_menu)
                popupMenu.setOnMenuItemClickListener {


                    when (it.itemId) {

                        R.id.delete_menu -> {

                            val dialog = AlertDialog.Builder(requireContext())
                            dialog.setMessage("${citizens.name} ${citizens.passportSeriya} fuqaro o'chirilsinmi ?")
                            dialog.setNegativeButton("Ha") { dialog, which ->

                                val id = appDatabase.citizenDao()
                                    .getCitizenById(citizens.passportSeriya!!)
                                citizens.id = id
                                appDatabase.citizenDao().deleteCitizen(citizens)
                                Toast.makeText(
                                    requireContext(),
                                    "${citizens.id} deleted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onResume()

                            }

                            dialog.setPositiveButton("Yo'q") {

                                    dialog, which ->

                            }

                            dialog.show()

                        }


                        R.id.edit_menu -> {

                            findNavController().navigate(
                                R.id.editFragment,
                                bundleOf("citizensKey" to citizens)
                            )

                        }

                    }

                    true

                }

                popupMenu.show()

            }

        })

        binding.rvList.adapter = rvAdapter

    }

}