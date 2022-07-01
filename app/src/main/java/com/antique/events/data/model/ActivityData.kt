package com.antique.events.data.model

data class ActivityData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val department: String,
    val description: String,
    val endDateTime: String,
    val participants: List<Participant>,
    val registeredUsers: List<RegisteredUser>,
    val startDateTime: String,
    val title: String
)