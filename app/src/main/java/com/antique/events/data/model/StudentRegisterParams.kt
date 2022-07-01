package com.antique.events.data.model

data class StudentRegisterParams(
    val course: String?,
    val department: String?,
    val email: String,
    val fname: String,
    val gender: String,
    val lname: String,
    val mname: String,
    val password: String,
    val type: String,
    val yearLevel: String
)