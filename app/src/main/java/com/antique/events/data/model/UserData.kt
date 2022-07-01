package com.antique.events.data.model

data class UserData(
    val __v: Int,
    val _id: String,
    val course: String? = "N/A",
    val createdAt: String,
    val department: String? = "N/A",
    val email: String,
    val fname: String,
    val gender: String,
    val lname: String,
    val mname: String,
    val type: String,
    val yearLevel: String
)