package com.antique.events.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var startDateTime: String = "",
    var endDateTime: String = "",
    var participants: List<Participant> = listOf(),
    var registeredUsers: List<RegisteredUser> = listOf(),
    var department: String = ""
) : Parcelable