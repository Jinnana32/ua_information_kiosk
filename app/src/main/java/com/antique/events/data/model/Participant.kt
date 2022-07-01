package com.antique.events.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Participant(
    val _id: String,
    val createdAt: String,
    val email: String,
    val fname: String,
    val lname: String,
    val mname: String,
    val type: String
): Parcelable