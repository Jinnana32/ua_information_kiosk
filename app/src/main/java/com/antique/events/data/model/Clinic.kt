package com.antique.events.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
class Clinic(
    var id: String = "",
    var type: Int = 0,
    var name: String = "",
    var address: String = "",
    var contactNumber: String = "",
    var description: String = "",
    var doctorName: String = "",
    var specialization: String = "",
    var emailAddress: String = "",
    var openingTime: String? = "",
    var closingTime: String? = "",
    var openingDays: String? = "",
    var consultaionRate: Int? = 0,
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
) : Parcelable