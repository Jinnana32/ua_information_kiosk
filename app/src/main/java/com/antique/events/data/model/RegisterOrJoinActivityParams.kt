package com.antique.events.data.model

data class RegisterOrJoinActivityParams(
    val email: String,
    val fname: String,
    val lname: String,
    val mname: String,
    val type: String
)