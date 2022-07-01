package com.antique.events.ui.dashboard.appointment.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import com.antique.events.R
import com.antique.events.data.model.Appointment
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.utils.adapter.AppointmentClinicVerticalAdapterItem
import com.antique.events.ui.utils.adapter.AppointmentVerticalAdapterItem
import com.xub.lakad.presentation.common.libs.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_today_appointment.*

class TodayAppoinmentFragment : Fragment() {

    companion object {
        fun getInstance() : Fragment{
            val fragment = TodayAppoinmentFragment()
            return fragment
        }
    }

    private lateinit var appointmentVerticalAdapterItem: FastItemAdapter<AppointmentVerticalAdapterItem>
    private lateinit var appointmentClinicVerticalAdapterItem: FastItemAdapter<AppointmentClinicVerticalAdapterItem>

    private var BARCODE_BOTTOM_SHEET_TAG = "BARCODE_BOTTOM_SHEET_TAG"
    private lateinit var barcodeBottomSheetDialog: BarcodeBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_today_appointment, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appointmentClinicVerticalAdapterItem = FastItemAdapter();
        rv_appoint_today_clinic.itemAnimator = SlideDownAlphaAnimator()
        rv_appoint_today_clinic.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_appoint_today_clinic.addItemDecoration(VerticalSpaceItemDecoration(30))
        rv_appoint_today_clinic.adapter = appointmentClinicVerticalAdapterItem;

        appointmentVerticalAdapterItem = FastItemAdapter();
        rv_appointment_today.itemAnimator = SlideDownAlphaAnimator()
        rv_appointment_today.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_appointment_today.addItemDecoration(VerticalSpaceItemDecoration(30))
        rv_appointment_today.adapter = appointmentVerticalAdapterItem;


    }

    private fun initClinicAppointmentItems(appointments: List<Appointment?>) {
        appointmentClinicVerticalAdapterItem.clear()
        for (appointment in appointments) {
            appointmentClinicVerticalAdapterItem.add(
                AppointmentClinicVerticalAdapterItem(appointment!!, false) {

                }
            )
        }
        appointmentClinicVerticalAdapterItem.withOnClickListener{view, adapter, item, position ->
            //startActivity(EventDetailsActivity.getIntent(requireContext(), appointment = item.model));
            true;
        }
        appointmentClinicVerticalAdapterItem.notifyAdapterDataSetChanged()
    }

    private fun initAppointmentItems(appointments: List<Appointment?>) {
        appointmentVerticalAdapterItem.clear()
        for (appointment in appointments) {
            appointmentVerticalAdapterItem.add(
                AppointmentVerticalAdapterItem(appointment!!, false) {

                }
            )
        }
        appointmentVerticalAdapterItem.withOnClickListener{view, adapter, item, position ->
            barcodeBottomSheetDialog = BarcodeBottomSheetDialog.newInstance(item.model.id).withOnDialogClose {
                barcodeBottomSheetDialog.dismiss();
            };
            barcodeBottomSheetDialog.show(childFragmentManager, BARCODE_BOTTOM_SHEET_TAG)
            true;
        }
        appointmentVerticalAdapterItem.notifyAdapterDataSetChanged()
    }

    // Fetch Appointments
    private lateinit var appointmentRef: DatabaseReference;
    private fun fetchAppointments(userType: Int = 1) {

    }

}