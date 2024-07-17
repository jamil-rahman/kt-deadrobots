package com.example.deadrobotrestaurant

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class OrderActivity : AppCompatActivity() {
    private lateinit var etFullName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etStreetAddress: EditText
    private lateinit var etAptSuite: EditText
    private lateinit var etCity: EditText
    private lateinit var spinnerProvince: Spinner
    private lateinit var etPostalCode: EditText
    private lateinit var btnPlaceOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)

        // Initialize views
        etFullName = findViewById(R.id.etFullName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etStreetAddress = findViewById(R.id.etStreetAddress)
        etAptSuite = findViewById(R.id.etAptSuite)
        etCity = findViewById(R.id.etCity)
        spinnerProvince = findViewById(R.id.spinnerProvince)
        etPostalCode = findViewById(R.id.etPostalCode)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)

        // Set up province spinner
        setupProvinceSpinner()

        // Set up place order button click listener
        btnPlaceOrder.setOnClickListener {
            if (validateForm()) {
                placeOrder()
            }
        }
    }

    private fun setupProvinceSpinner() {
        val provinces = arrayOf("Select Province", "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Northwest Territories", "Nunavut", "Yukon")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvince.adapter = adapter
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate Full Name
        if (etFullName.text.toString().trim().isEmpty()) {
            etFullName.error = "Full Name is required"
            isValid = false
        }

        // Validate Phone Number
        if (etPhoneNumber.text.toString().trim().isEmpty()) {
            etPhoneNumber.error = "Phone Number is required"
            isValid = false
        }

        // Validate Street Address
        if (etStreetAddress.text.toString().trim().isEmpty()) {
            etStreetAddress.error = "Street Address is required"
            isValid = false
        }

        // Validate City
        if (etCity.text.toString().trim().isEmpty()) {
            etCity.error = "City is required"
            isValid = false
        }

        // Validate Province
        if (spinnerProvince.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a province", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Validate Postal Code
        val postalCode = etPostalCode.text.toString().trim()
        if (postalCode.isEmpty()) {
            etPostalCode.error = "Postal Code is required"
            isValid = false
        } else if (!isValidCanadianPostalCode(postalCode)) {
            etPostalCode.error = "Invalid Postal Code format"
            isValid = false
        }

        return isValid
    }

    private fun isValidCanadianPostalCode(postalCode: String): Boolean {
        //source for candian postal regex: https://stackoverflow.com/questions/15774555/efficient-regex-for-canadian-postal-code-function
        val regex = "^[ABCEGHJ-NPRSTVXY]\\d[ABCEGHJ-NPRSTV-Z][ -]?\\d[ABCEGHJ-NPRSTV-Z]\\d\$".toRegex()
        return regex.matches(postalCode)
    }

    private fun placeOrder() {
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show()
        //TODO create db entry for orders collection.
        //TODO REMOVE cart data from db once order entry is created
        //TODO Navigate to Success page once order is created successfully
    }
}