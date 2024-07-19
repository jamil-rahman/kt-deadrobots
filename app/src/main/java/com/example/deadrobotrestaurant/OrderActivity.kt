package com.example.deadrobotrestaurant
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class OrderActivity : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var streetAddress: EditText
    private lateinit var aptSuite: EditText
    private lateinit var city: EditText
    private lateinit var province: Spinner
    private lateinit var postalEdit: EditText
    private lateinit var btnPlaceOrder: Button
    private lateinit var cardNumber: EditText
    private lateinit var expirationDate: EditText
    private lateinit var cvv: EditText
    private lateinit var cardName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)

        fullName = findViewById(R.id.editFullName)
        phoneNumber = findViewById(R.id.editPhoneNumber)
        streetAddress = findViewById(R.id.editStreetAddress)
        aptSuite = findViewById(R.id.editAptSuite)
        city = findViewById(R.id.editCity)
        province = findViewById(R.id.spinnerProvince)
        postalEdit = findViewById(R.id.editPostalCode)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        cardNumber = findViewById(R.id.editCardNumber)
        expirationDate = findViewById(R.id.editExpirationDate)
        cvv = findViewById(R.id.editCVV)
        cardName = findViewById(R.id.editCardName)

        setupProvinceSpinner()

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
        province.adapter = adapter
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate Full Name
        if (fullName.text.toString().trim().isEmpty()) {
            fullName.error = "Full Name is required"
            isValid = false
        }

        // Validate Phone Number
        if (phoneNumber.text.toString().trim().isEmpty()) {
            phoneNumber.error = "Phone Number is required"
            isValid = false
        }

        // Validate Street Address
        if (streetAddress.text.toString().trim().isEmpty()) {
            streetAddress.error = "Street Address is required"
            isValid = false
        }

        // Validate City
        if (city.text.toString().trim().isEmpty()) {
            city.error = "City is required"
            isValid = false
        }

        // Validate Province
        if (province.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a province", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Validate Postal Code
        val postalCode = postalEdit.text.toString().trim()
        if (postalCode.isEmpty()) {
            postalEdit.error = "Postal Code is required"
            isValid = false
        } else if (!isValidCanadianPostalCode(postalCode)) {
            postalEdit.error = "Invalid Postal Code format"
            isValid = false
        }

        // Validate Cardholder Name
        if (cardName.text.toString().trim().isEmpty()) {
            cardName.error = "Cardholder Name is required"
            isValid = false
        }

        // Validate Card Number
        val cardNum = cardNumber.text.toString().trim()
        if (cardNum.isEmpty()) {
            cardNumber.error = "Card Number is required"
            isValid = false
        } else if (!isValidCardNumber(cardNum)) {
            cardNumber.error = "Invalid Card Number format"
            isValid = false
        }

        // Validate Expiration Date
        val expDate = expirationDate.text.toString().trim()
        if (expDate.isEmpty()) {
            expirationDate.error = "Expiration Date is required"
            isValid = false
        } else if (!isValidExpirationDate(expDate)) {
            expirationDate.error = "Invalid Expiration Date format"
            isValid = false
        }

        // Validate CVV
        val cVV = cvv.text.toString().trim()
        if (cVV.isEmpty()) {
            cvv.error = "CVV is required"
            isValid = false
        } else if (!isValidCVV(cVV)) {
            cvv.error = "Invalid CVV format"
            isValid = false
        }


        return isValid
    }

    private fun isValidCanadianPostalCode(postalCode: String): Boolean {
        //source for canadian postal regex: https://stackoverflow.com/questions/15774555/efficient-regex-for-canadian-postal-code-function
        val regex = "^[ABCEGHJ-NPRSTVXY]\\d[ABCEGHJ-NPRSTV-Z][ -]?\\d[ABCEGHJ-NPRSTV-Z]\\d\$".toRegex()
        return regex.matches(postalCode)
    }

    // function to validate Expiration Date (MM/YY)
    private fun isValidExpirationDate(expirationDate: String): Boolean {
        // Expiration Date format: MM/YY
        return expirationDate.matches(Regex("^(0[1-9]|1[0-2])/[0-9]{2}$"))
    }

    // function to validate CVV
    private fun isValidCVV(cvv: String): Boolean {
        // CVV format: 3 or 4 digits
        return cvv.matches(Regex("^[0-9]{3,4}$"))
    }
    private fun isValidCardNumber(cardNumber: String): Boolean {
        // Remove any spaces or hyphens
        val cleanCardNumber = cardNumber.replace(Regex("[\\s-]"), "")
        // Check if the length is between 13 and 19 digits
        if (cleanCardNumber.length < 13 || cleanCardNumber.length > 19) {
            return false
        }
        return true
    }
    private fun placeOrder() {
        val fullName = fullName.text.toString().trim()
        val phoneNumber = phoneNumber.text.toString().trim()
        val streetAddress = streetAddress.text.toString().trim()
        val aptSuite = aptSuite.text.toString().trim()
        val city = city.text.toString().trim()
        val province = province.selectedItem.toString().trim()
        val postalCode = postalEdit.text.toString().trim()

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cart").child(userId)
        val ordersRef = database.getReference("orders").child(userId)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formattedDate = current.format(formatter)


        cartRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val cartSnapshot = task.result
                if (cartSnapshot.exists()) {
                    val cartItems = mutableListOf<CartItem>()
                    for (itemSnapshot in cartSnapshot.children) {
                        val title = itemSnapshot.child("title").getValue(String::class.java) ?: ""
                        val image = itemSnapshot.child("image").getValue(String::class.java) ?: ""
                        val quantity = itemSnapshot.child("quantity").getValue(Int::class.java) ?: 0
                        val cartItem = CartItem(title, image, quantity)
                        cartItems.add(cartItem)
                    }

                    val order = Order(
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        streetAddress = streetAddress,
                        aptSuite = aptSuite,
                        city = city,
                        province = province,
                        postalCode = postalCode,
                        orderDate = formattedDate,
                        cartItems = cartItems
                    )

                    ordersRef.push().setValue(order).addOnCompleteListener { orderTask ->
                        if (orderTask.isSuccessful) {
                            cartRef.removeValue().addOnCompleteListener { clearTask ->
                                if (clearTask.isSuccessful) {
                                    Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
                                    // Navigate to PaymentActivity
                                    val intent = Intent(this, SuccessActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to clear cart", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to get cart items", Toast.LENGTH_SHORT).show()
            }
        }
    }
}