package com.antique.events.data.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class User constructor(
    var id: String? = "",
    var type: Int? = 0,
    var firstName: String = "",
    var lastName: String = "",
    var middleName: String = "",
    var birthdate: String? = "",
    var gender: String = "",
    var yearLevel: String = "",
    var course: String = "",
    var department: String = "",
    var contactNumber: String? = "",
    var presentAddress: String? = "",
    var emailAddress: String = "",
    var password: String? = ""
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "type" to type,
            "firstName" to firstName,
            "lastName" to lastName,
            "middleName" to middleName,
            "birthdate" to birthdate,
            "gender" to gender,
            "contactNumber" to contactNumber,
            "presentAddress" to presentAddress,
            "emailAddress" to emailAddress
        )
    }
}
