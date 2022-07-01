package com.antique.events.ui.appointment

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter
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
import com.antique.events.data.model.Clinic
import com.antique.events.data.model.UnavailableDate
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.utils.adapter.AppointmentTakenAdapterItem
import com.antique.events.ui.utils.adapter.UnavailableDateAdapterItem
import com.antique.events.ui.dashboard.MainActivity
import com.antique.events.ui.dashboard.appointment.fragments.BarcodeBottomSheetDialog
import com.antique.events.ui.utils.helper.DatePickerHelper
import com.antique.events.ui.utils.helper.DateUtil
import com.antique.events.ui.utils.helper.DateUtil.getDateFromTime
import com.antique.events.ui.utils.helper.DateUtil.isPastDate
import com.antique.events.ui.utils.helper.DateUtil.isWithinDates
import com.antique.events.ui.utils.helper.DateUtil.parseDate
import com.antique.events.ui.utils.helper.DialogLoader
import com.xub.lakad.presentation.common.libs.HorizontalSpaceItemDecoration
import kotlinx.android.synthetic.main.activity_appointment_form.*
import java.util.*

class AppointmentFormActivity : AppCompatActivity() {

    companion object {
        private val CLINIC_DETAILS = "CLINIC_DETAILS";
        fun getIntent(context: Context, clinic: Clinic): Intent {
            return Intent(context, AppointmentFormActivity::class.java)
                .putExtra(CLINIC_DETAILS, clinic);
        }
    }

    private val APPOINTMENT_DATE_TAG = "APPOINTMENT_DATE_TAG";
    private val APPOINTMENT_TIME_TAG = "APPOINTMENT_TIME_TAG";
    private lateinit var rootRef: DatabaseReference;
    private lateinit var appointmentReference: DatabaseReference

    private lateinit var loadingDialog: Dialog;
    var clinic: Clinic? = null;
    val unavailableNotWholeDays: MutableList<Pair<String, String>> = mutableListOf();
    val bookedTimeSlots : MutableList<Appointment> = mutableListOf();

    private var BARCODE_BOTTOM_SHEET_TAG = "BARCODE_BOTTOM_SHEET_TAG"
    private lateinit var barcodeBottomSheetDialog: BarcodeBottomSheetDialog

    private lateinit var unavailableDateAdapterItem : FastItemAdapter<UnavailableDateAdapterItem>;
    private lateinit var appointmentTakenAdapterItem : FastItemAdapter<AppointmentTakenAdapterItem>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_form)

        clinic =
            this@AppointmentFormActivity.intent.extras!!.getParcelable<Clinic>(CLINIC_DETAILS);

        val toolbar: Toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar !== null) {
            supportActionBar!!.title = "Appointment Form";
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener {
                finish();
            }
        }

        et_time_pick.setOnClickListener {
            DatePickerHelper.showTimePicker(supportFragmentManager, APPOINTMENT_TIME_TAG) {
                val currentDateString = et_date_pick.text.toString();
                val dateTimePicked = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_TIME_HM);
                val currentDate = parseDate(currentDateString, DateUtil.GOOD_FORMAT_DATE);
                val selectedTime = getDateFromTime(currentDate, dateTimePicked);
                var hasOpeningConflict = false;
                var hasClosingConflict = false;

                // check for taken timeslots
                if(bookedTimeSlots.isNotEmpty()) {
                    var takenConflict = false;
                    bookedTimeSlots.forEach {
                        val timeSlot : String = DateUtil.parseDate(it.date, DateUtil.GOOD_FORMAT_DATE_2, DateUtil.GOOD_FORMAT_TIME_HM); // Sep 21, 2021 01:00 PM
                        val takenStartTime : Date = getDateFromTime(currentDate, timeSlot); // 01:00 PM
                        val calendarDate = Calendar.getInstance()
                        calendarDate.time = takenStartTime;
                        val timeInSecs = calendarDate.timeInMillis
                        val takenEndTime = Date(timeInSecs + 10 * 60 * 1000) // 10 minutes = 01:10 PM
                        val isDateWithin = isWithinDates(selectedTime, takenStartTime, takenEndTime);
                        if(isDateWithin){
                            takenConflict = true;
                        }
                    }
                    if(takenConflict){
                        Log.e("conflict", "Taken conflict");
                        hasOpeningConflict = true;
                        hasClosingConflict = true;
                        Toast.makeText(
                            this@AppointmentFormActivity,
                            "Please make sure to make an appointment not less than 10 minutes of the taken timeslots.",
                            Toast.LENGTH_LONG
                        ).show();
                    }
                }

                // [10:20-10:30, 11:30-11:40]
                // check for unavailable dates
                if(unavailableNotWholeDays.isNotEmpty()){
                    var unavailableConflict = false;
                    unavailableNotWholeDays.forEach {
                        val matchStartTime = it!!.first;
                        val matchEndTime = it!!.second;
                        val startOfLeave: Date = getDateFromTime(currentDate, matchStartTime);
                        val endOfLeave: Date = getDateFromTime(currentDate, matchEndTime);
                        val isDateWithin = isWithinDates(selectedTime, startOfLeave, endOfLeave);
                        if(isDateWithin){
                            unavailableConflict = true;
                        }
                    }
                    if(unavailableConflict) {
                        Log.e("conflict", "unavailable conflict");
                        hasOpeningConflict = true;
                        hasClosingConflict = true;
                        Toast.makeText(
                            this@AppointmentFormActivity,
                            "Sorry time conflict. Please check doctor's unavailable dates",
                            Toast.LENGTH_LONG
                        ).show();
                    }
                }

                // opening and closing
                val openingTime: Date = getDateFromTime(currentDate, clinic!!.openingTime!!);
                val closingTime: Date = getDateFromTime(currentDate, clinic!!.closingTime!!);
                // validate if selected time is past of opening time
                val openingConflict = isPastDate(selectedTime, openingTime);
                val closingConflict = isPastDate(selectedTime, closingTime);
                if(openingConflict <= 0) {
                    Log.e("conflict", "start time conflict");
                    hasOpeningConflict = true
                    Toast.makeText(
                        this@AppointmentFormActivity,
                        "Please select a time not earlier than ${clinic!!.openingTime}",
                        Toast.LENGTH_LONG
                    ).show();
                };
                if(closingConflict >= 0) {
                    Log.e("conflict", "end time conflict");
                    hasClosingConflict = true
                    Toast.makeText(
                        this@AppointmentFormActivity,
                        "Please select a time before ${clinic!!.closingTime}",
                        Toast.LENGTH_LONG
                    ).show();
                };

                if(hasOpeningConflict){
                    et_time_pick.setText("");
                    form_appointment.visibility = View.GONE;
                } else if (hasClosingConflict) {
                    et_time_pick.setText("");
                    form_appointment.visibility = View.GONE;
                } else {
                    et_time_pick.setText(dateTimePicked);
                    form_appointment.visibility = View.VISIBLE;
                }

            }
        }

        // init clinic details
        form_clinic_name.text = clinic!!.name;
        form_clinic_doctor.text = clinic!!.doctorName;
        form_clinic_specialization.text = clinic!!.specialization;
        form_clinic_address.text = clinic!!.address;
        form_clinic_opening.text = clinic!!.openingTime;
        form_clinic_closing.text = clinic!!.closingTime;

        unavailableDateAdapterItem = FastItemAdapter();
        rv_clinic_unavailable_days.itemAnimator = SlideDownAlphaAnimator()
        rv_clinic_unavailable_days.layoutManager =
            LinearLayoutManager(this@AppointmentFormActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_clinic_unavailable_days.addItemDecoration(HorizontalSpaceItemDecoration(20))
        rv_clinic_unavailable_days.adapter = unavailableDateAdapterItem;

        this.fetchUnavailableDates(clinic!!);

        form_appointment.setOnClickListener {
            loadingDialog = DialogLoader.createProgressDialog(this@AppointmentFormActivity)!!;
            loadingDialog.show();
            try {
                val currentDate = "${et_date_pick.editableText} ${et_time_pick.editableText}";
            }catch (err: Error){
                Log.e("Android debug", err.message.toString());
            }

        }
    }

    // Fetch Appointments
    private lateinit var appointmentRef: DatabaseReference;
    private fun fetchAppointments(userType: Int = 1) {
        // clinic data listener
        appointmentRef = Firebase.database.reference.child("appointments");
        val appointmentValueListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val fetchedAppointment: Appointment? = postSnapshot.getValue<Appointment>()!!;
                    val formattedDate = DateUtil.parseDate(
                        fetchedAppointment!!.date,
                        DateUtil.GOOD_FORMAT_DATE_2,
                        DateUtil.GOOD_FORMAT_DATE
                    );
                    if(fetchedAppointment.clinicKey == clinic!!.id  && et_date_pick.text.toString() == formattedDate){
                        bookedTimeSlots.add(fetchedAppointment);
                    }
                }
                if(bookedTimeSlots.isNotEmpty()){
                    Log.e("Android debug", bookedTimeSlots.toString());
                    tv_booked_title.visibility = View.VISIBLE;
                    rv_booked_appointment.visibility = View.VISIBLE;
                    initAppointmentItems(bookedTimeSlots);
                }else{
                    tv_booked_title.visibility = View.GONE;
                    rv_booked_appointment.visibility = View.GONE;
                }

                et_time_pick.visibility = View.VISIBLE;
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        appointmentRef.addValueEventListener(appointmentValueListener)
    }

    private fun initAppointmentItems(appointments: List<Appointment?>) {
        appointmentTakenAdapterItem = FastItemAdapter();
        rv_booked_appointment.itemAnimator = SlideDownAlphaAnimator()
        rv_booked_appointment.layoutManager =
            LinearLayoutManager(this@AppointmentFormActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_booked_appointment.addItemDecoration(HorizontalSpaceItemDecoration(20))
        rv_booked_appointment.adapter = appointmentTakenAdapterItem;

        appointmentTakenAdapterItem.clear()
        for (appointment in appointments) {
            appointmentTakenAdapterItem.add(
                AppointmentTakenAdapterItem(appointment!!.date)
            )
        }
        appointmentTakenAdapterItem.notifyAdapterDataSetChanged()
    }

    // Fetch Appointments
    private lateinit var unavailableRef: DatabaseReference;
    private fun fetchUnavailableDates(clinic: Clinic) {
        // clinic data listener
        unavailableRef = Firebase.database.reference.child("unavailable_date");
        val clinicsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var disabledDays: SparseArray<MonthAdapter.CalendarDay> = SparseArray();

                val disabledWeekdays = clinic.openingDays!!.split(",").map { it.toInt() }.toList();
                val unavailableDates : MutableList<Date> = mutableListOf();
                val unavailableDatesData : MutableList<UnavailableDate?> = mutableListOf();
                // Fetch user data from firebase
                for (postSnapshot in dataSnapshot.children) {
                    val fetchedDate: UnavailableDate? = postSnapshot.getValue<UnavailableDate>()!!;
                    if(fetchedDate!!.clinicKey == clinic.id){
                        unavailableDatesData.add(fetchedDate);
                        if(fetchedDate.notWholeDay){
                            unavailableNotWholeDays.add(Pair(
                                fetchedDate.startTime!!,
                                fetchedDate.endTime!!
                            ));
                        }else{
                            val parsedDate = DateUtil.parseDate(
                                fetchedDate.date,
                                DateUtil.GOOD_FORMAT_DATE
                            );
                            unavailableDates.add(parsedDate);
                        }
                    }
                }

                if(unavailableDates.isNotEmpty()) {
                    disabledDays = DatePickerHelper.parseDisabledDays(unavailableDates);
                }

                if(unavailableDatesData.isNotEmpty()){
                    initUnavailableItems(unavailableDatesData);
                }

                et_date_pick.setOnClickListener {
                    DatePickerHelper.showDatePicker(
                        supportFragmentManager,
                        APPOINTMENT_DATE_TAG,
                        disabledDays = disabledDays,
                        disabledWeekdays = disabledWeekdays
                    ) {
                        val datePicked = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_DATE);
                        et_date_pick.setText(datePicked);
                        this@AppointmentFormActivity.fetchAppointments();
                    }
                }

                Log.e("Android debug", "${unavailableNotWholeDays}");
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        unavailableRef.addValueEventListener(clinicsListener)
    }

    private fun initClinicUnavailableDays() {
        layout_clinic_unavailable.visibility = View.VISIBLE;
    }

    private fun initUnavailableItems(unavailableDates: List<UnavailableDate?>) {
        unavailableDateAdapterItem.clear()
        for (date in unavailableDates) {
            unavailableDateAdapterItem.add(
                UnavailableDateAdapterItem(date!!)
            )
        }
        unavailableDateAdapterItem.notifyAdapterDataSetChanged()
        initClinicUnavailableDays();
    }

}