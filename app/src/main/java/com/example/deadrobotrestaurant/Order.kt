package com.example.deadrobotrestaurant

data class Order(
    val fullName: String,
    val phoneNumber: String,
    val streetAddress: String,
    val aptSuite: String,
    val city: String,
    val province: String,
    val postalCode: String,
    val orderDate: String,
    val cartItems: List<CartItem>
)