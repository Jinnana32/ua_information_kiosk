package com.antique.events.data.model

data class StudentUpdateParams(
    val course: String,
    val department: String,
    val email: String,
    val fname: String,
    val gender: String,
    val lname: String,
    val mname: String,
    val yearLevel: String
)