package com.example.countryapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.countryapp.databinding.ActivityMainBinding
import com.example.countryapp.databinding.DialogExitBinding
import java.util.Calendar

class MainActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var provinces: Array<String>
    private val countries = arrayOf(
        "Indonesia",
        "Malaysia",
        "Singapore",
        "Thailand",
        "Vietnam",
        "Laos",
        "Cambodia",
        "Brunei",
        "Myanmar",
        "Timor-Leste"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktifkan view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil array dari resources (arrays.xml)
        provinces = resources.getStringArray(R.array.provinces)

        with(binding) {
            // Adapter untuk spinnerCountry
            val adapterCountry = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_item,
                countries
            )
            adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCountry.adapter = adapterCountry

            // Adapter untuk spinnerProvinces
            val adapterProvinces = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_item,
                provinces
            )
            adapterProvinces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerProvinces.adapter = adapterProvinces

            // Listener spinner country
            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        countries[position],
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            // Listener DatePicker widget di layout
            datePicker.init(datePicker.year, datePicker.month, datePicker.dayOfMonth) { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                Toast.makeText(this@MainActivity, selectedDate, Toast.LENGTH_SHORT).show()
            }

            // Listener TimePicker widget di layout
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                Toast.makeText(this@MainActivity, selectedTime, Toast.LENGTH_SHORT).show()
            }

            // Button untuk menampilkan DatePickerDialog
            btnShowDatePicker.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, "datePicker")
            }

            // Button untuk menampilkan TimePickerDialog
            btnShowTimePicker.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, "timePicker")
            }
            btnShowAlertDialog.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Keluar")
                builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                builder.setPositiveButton("Ya") { dialog, which ->
                    //lakukan sesuatu ketika tombol positif diklik
                    finish()
                }
                builder.setNegativeButton("Tidak") { dialog, _ ->
                    //lakukan sesuatu ketika tombol negatif diklik
                    dialog.dismiss()
                }
                // Membuat dan menampilkan dialog
                val dialog = builder.create()
                dialog.show()
            }
            btnShowCustomDialog .setOnClickListener {
                val dialogExit = DialogExit()
                dialogExit.show(supportFragmentManager, "dialogExit")
            }
        }
    }

    // Listener untuk DatePickerDialog
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$dayOfMonth/${month + 1}/$year"
        Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
    }

    // Listener untuk TimePickerDialog
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
    }
}

// Fragment untuk DatePickerDialog
class DatePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val monthOfYear = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireActivity(),
            activity as DatePickerDialog.OnDateSetListener,
            year,
            monthOfYear,
            dayOfMonth
        )
    }
}

// Fragment untuk TimePickerDialog
class TimePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireActivity(),
            activity as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            DateFormat.is24HourFormat(requireActivity())
        )
    }
}
class DialogExit : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val binding = DialogExitBinding.inflate(inflater)
        with(binding){
            btnYes.setOnClickListener {
                requireActivity().finish()
            }
            btnNo.setOnClickListener {
                dismiss()
            }
        }
        builder.setView(binding.root)
        return builder.create()
    }
}
