package com.antique.events.data.model

import android.os.Parcelable
import com.antique.events.R
import kotlinx.android.parcel.Parcelize

@Parcelize
class Appointment (
    var id: String = "",
    var clinicKey: String = "",
    var userKey: String? = "",
    var date: String = "",
    var doctor: String = "",
    var position: String = "",
    var remainingTime: String = "",

    // User details
    var patientName: String? = "",
    var patientAddress: String? = "",
    var patientContactNumber: String? = "",
    var patientEmail: String? = "",

    // Clinic details
    var clinicName: String? = "",
    var clinicAddress: String? = "",
    var clinicContactNum: String? = "",
    var clinicRate: Int? = 0,

    // 0 = Pending
    // 1 = History
    // 2 = Cancelled Pending
    // 3 = Cancelled
    var status: Int? = 0

    ) : Parcelable {

    companion object {
        val PENDING_APPOINTMENT = 0;
        val HISTORY_APPOINTMENT = 1;
        val CANCELLED_PENDING_APPOINTMENT = 2;
        val CANCELLED_APPOINTMENT = 3;
    }

    override fun toString() : String {
        if(status == CANCELLED_APPOINTMENT) return "CANCELLED";
        if(status == HISTORY_APPOINTMENT) return "DONE";
        return "PENDING";
    }

    fun getStatusColor() : Int {
        if(status == CANCELLED_APPOINTMENT) return R.color.md_red_600;
        if(status == HISTORY_APPOINTMENT) return R.color.md_green_700;
        return R.color.md_deep_orange_700;
    }
}

