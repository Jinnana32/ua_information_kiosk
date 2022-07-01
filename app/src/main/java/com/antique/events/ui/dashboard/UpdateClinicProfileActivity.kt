package com.antique.events.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.antique.events.R
import com.antique.events.data.model.Clinic
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.utils.helper.DatePickerHelper
import com.antique.events.ui.utils.helper.DateUtil
import com.antique.events.ui.utils.helper.DialogLoader
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_friday
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_monday
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_saturday
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_sunday
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_thurs
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_tuesday
import kotlinx.android.synthetic.main.activity_update_clinic_profile.cb_weds
import kotlinx.android.synthetic.main.activity_update_clinic_profile.et_closing_time
import kotlinx.android.synthetic.main.activity_update_clinic_profile.et_opening_time
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_address
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_contact
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_description
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_doctor
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_name
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_rate
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_clinic_specialization
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_confirm_password
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_email_address
import kotlinx.android.synthetic.main.activity_update_clinic_profile.reg_password
import kotlinx.android.synthetic.main.activity_update_clinic_profile.update_button
import kotlinx.android.synthetic.main.activity_update_profile.toolbar

class UpdateClinicProfileActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UpdateClinicProfileActivity::class.java);
        }
    }

    private var OPENING_TIME_TAG = "OPENING_TIME_TAG"
    private var CLOSING_TIME_TAG = "CLOSING_TIME_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_clinic_profile)
        if (supportActionBar !== null) {
            supportActionBar!!.setTitle("Register");
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener {
                finish();
            }
        }

        initProfile();

        update_button.setOnClickListener {

        }
    }

    private fun getWeekdays() : String {
        val weekdays = mutableListOf<String>();
        if(cb_monday.isChecked) weekdays.add("2");
        if(cb_tuesday.isChecked) weekdays.add("3");
        if(cb_weds.isChecked) weekdays.add("4");
        if(cb_thurs.isChecked) weekdays.add("5");
        if(cb_friday.isChecked) weekdays.add("6");
        if(cb_saturday.isChecked) weekdays.add("7");
        if(cb_sunday.isChecked) weekdays.add("1");
        return weekdays.joinToString(separator = ",");
    }

    private fun initCheckBox(days: String) {
        val openingDays = days.split(",");
        openingDays.forEach {
            when(it) {
                "1" -> cb_sunday.isChecked = true
                "2" -> cb_monday.isChecked = true
                "3" -> cb_tuesday.isChecked = true
                "4" -> cb_weds.isChecked = true
                "5" -> cb_thurs.isChecked = true
                "6" -> cb_friday.isChecked = true
                "7" -> cb_saturday.isChecked = true
            }
        }
    }

    private fun initProfile() {
        et_opening_time.setOnClickListener {
            DatePickerHelper.showTimePicker(supportFragmentManager, OPENING_TIME_TAG) {
                val pickedTime = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_TIME_HM);
                et_opening_time.setText(pickedTime);
            }
        }

        et_closing_time.setOnClickListener {
            DatePickerHelper.showTimePicker(supportFragmentManager, CLOSING_TIME_TAG) {
                val dateTimePicked = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_TIME_HM);
                et_closing_time.setText(dateTimePicked);
            }
        }
    }
}