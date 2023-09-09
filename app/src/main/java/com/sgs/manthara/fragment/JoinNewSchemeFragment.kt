package com.sgs.manthara.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.activity.DashBoardActivity
import com.sgs.manthara.databinding.FragmentJoinNewSchemeBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JoinNewSchemeFragment : Fragment() {
    private lateinit var binding: FragmentJoinNewSchemeBinding
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var mainPreference: MainPreference
    private lateinit var etAmount: EditText
    private lateinit var etTotalAmount: EditText
    private var formattedDate = ""
    private var lt = ""
    private var ln = ""
    private var scheme = ""
    private var schemeName = ""
    private var nameList: MutableList<String> = mutableListOf()
    private var idList: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainPreference = MainPreference(requireContext())
        binding = FragmentJoinNewSchemeBinding.inflate(inflater, container, false)
        binding.tvDetails.text =
            Html.fromHtml("<font color='#000000'>Just a few things to get started</font><br><font color='#000000'>Let's Join</font> <font color='#00a29b'>new scheme</font>")

        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }

        binding.atvSDate.setOnClickListener {
            showAnniversaryPickerDialog()
        }

        binding.ibView.setOnClickListener {
            findNavController().navigate(R.id.viewPage)
        }

        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        formattedDate = dateFormat.format(currentDate.time)
        binding.atvSDate.setText(formattedDate)

        etAmount = binding.atvAmount
        etTotalAmount = binding.atvTotalAmount
        etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateTotalAmount()
            }
        })
        schemeTypeAuto()
        binding.btnSubmit.setOnClickListener {
            saveScheme()
        }

        return binding.root
    }

    private fun saveScheme() {
        when {
            binding.atvName.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Enter a name", Toast.LENGTH_SHORT).show()
            }

            binding.atvSchmeType.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Select Scheme Type", Toast.LENGTH_SHORT).show()
            }

            binding.atvSDate.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Select Date", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.atvAmount.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Enter the Amount", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.atvTotalAmount.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Enter the Total Amount", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                val deviceId =
                    Settings.Secure.getString(
                        requireContext().contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                lifecycleScope.launchWhenStarted {
                    jewelSoftVM.saveScheme(
                        "16",
                        mainPreference.getCid().first(),
                        deviceId,
                        ln,
                        lt,
                        mainPreference.getUserId().first(),
                        binding.atvName.text.toString(),
                        binding.atvEDate.text.toString(),
                        binding.atvRemark.text.toString(),
                        binding.atvEMI.text.toString(),
                        binding.atvFreeEMI.text.toString(),
                        binding.atvSDate.text.toString(),
                        schemeName,
                        scheme,
                        binding.atvAmount.text.toString(),
                        binding.atvTotalAmount.text.toString(),
                        binding.atvPurity.text.toString()

                    )
                }
                save()
            }
        }
    }

    private fun save() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.saveSchemeFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "saveError:${it.message} ")
                    }

                    is Resources.Success -> {
                        val i = it.data!!
                        if (!i.error) {
                            Log.i("TAG", "save:${it.data} ")
                        }
                        binding.atvName.setText("")
                        binding.atvSchmeType.setText("")
                        binding.atvEDate.setText("")
                        binding.atvEMI.setText("")
                        binding.atvFreeEMI.setText("")
                        binding.atvPurity.setText("")
                        binding.atvAmount.setText("")
                        binding.atvTotalAmount.setText("")
                        binding.atvRemark.setText("")

                        Toast.makeText(
                            requireContext(),
                            "Save Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val int = Intent(requireActivity(), DashBoardActivity::class.java)
                        startActivity(int)
                        requireActivity().finish()

                    }
                }
            }
        }
    }

    private fun schemeDetails(scheme: String) {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.enrollmentScheme(
                "11",
                mainPreference.getCid().first(),
                "2",
                scheme,
                formattedDate,
                deviceId, ln, lt,
                mainPreference.getUserId().first()
            )
        }
        autoFill()
    }

    private fun autoFill() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.enrollmentSchemeFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "autoErrorFill:${it.message.toString()}")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "autoFillSchemeSuccess:${it.data} ")

                        for (i in it.data!!) {
                            binding.atvEMI.setText(i.emi)
                            binding.atvFreeEMI.setText(i.free_emi)
                            binding.atvPurity.setText(i.metal_booked_purity)
                            binding.atvEDate.setText(i.mat_date)

                        }
                    }
                }
            }
        }
    }

    private fun schemeTypeAuto() {
        val deviceId =
            Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.schemeTypeAuto(
                "5",
                "4",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first()
            )
        }
        type()
    }

    private fun type() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.schemeTypeAutoFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme11:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "name: ${it.data}")
                        try {
                            nameList.clear()
                            idList.clear()
                            for (i in it.data!!) {
                                nameList.add(i.name)
                                idList.add(i.id)
                            }
                            val arrayAdapter = ArrayAdapter(
                                requireContext(),
                                R.layout.complete_text_view, nameList
                            )
                            binding.atvSchmeType.setAdapter(arrayAdapter)
                            binding.atvSchmeType.threshold = 1
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        binding.atvSchmeType.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                for (i in 0 until nameList.size) {
                    try {
                        if (binding.atvSchmeType.text.toString() == nameList[i]) {
                            scheme = idList[i]
                            schemeName = nameList[i]
                            schemeDetails(scheme)

                            Log.i("TAG", "forsalessss:$id")
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }
                }

            }
    }

    private fun calculateTotalAmount() {
        val enteredAmount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
        val totalAmount = enteredAmount * 12
        etTotalAmount.setText(totalAmount.toInt().toString())
    }

    private fun showAnniversaryPickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate =
                    "$dayOfMonth/${month + 1}/$year"
                binding.atvSDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.calendarViewShown = false
        datePickerDialog.datePicker.spinnersShown = true
        datePickerDialog.show()
    }
}