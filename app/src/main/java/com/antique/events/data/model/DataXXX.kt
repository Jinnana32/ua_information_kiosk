package com.antique.events.data.model

data class DataXXX(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val department: String,
    val description: String,
    val endDateTime: String,
    val participants: List<ParticipantX>,
    val registeredUsers: List<RegisteredUserX>,
    val startDateTime: String,
    val title: String
)