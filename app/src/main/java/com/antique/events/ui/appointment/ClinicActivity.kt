package com.antique.events.ui.appointment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
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
import com.antique.events.data.model.Clinic
import com.antique.events.data.model.UnavailableDate
import com.antique.events.ui.utils.adapter.UnavailableDateAdapterItem
import com.antique.events.ui.utils.helper.DateUtil
import com.xub.lakad.presentation.common.libs.HorizontalSpaceItemDecoration
import kotlinx.android.synthetic.main.activity_clinic.*

class ClinicActivity : AppCompatActivity() {

    companion object {
        private val CLINIC_DETAILS = "CLINIC_DETAILS";
        fun getIntent(context: Context, clinic: Clinic) : Intent {
            return Intent(context, ClinicActivity::class.java)
                .putExtra(CLINIC_DETAILS, clinic);
        }
    }

    private lateinit var unavailableDateAdapterItem : FastItemAdapter<UnavailableDateAdapterItem>;
    private lateinit var unavailableDateReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic)

        val clinic = this@ClinicActivity.intent.extras!!.getParcelable<Clinic>(CLINIC_DETAILS);
        if(clinic != null) this.initClinicDetails(clinic);

        val toolbar: Toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        if(supportActionBar !== null){
            supportActionBar!!.setTitle("Clinic Details");
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener {
                finish();
            }
        }

        btn_make_appointment.setOnClickListener {
            startActivity(AppointmentFormActivity.getIntent(this@ClinicActivity, clinic!!));
        }
    }

    private fun initClinicDetails(clinic: Clinic) {
        val clinicHours = "${clinic.openingTime} - ${clinic.closingTime}"
        val clinicRate = "Php ${clinic.consultaionRate.toString()}";
        tv_clinic_name.text = clinic.name;
        tv_item_position.text = clinic.emailAddress;
        tv_description.text = clinic.description;
        tv_address.text = clinic.address;
        tv_schedule.text = clinicHours
        tv_clinic_rate.text = clinicRate

        tv_clinic_work_days.text = clinic.openingDays!!.split(",").mapIndexed { index, c ->
            DateUtil.WEEKDAYS[index];
        }.joinToString()

        unavailableDateAdapterItem = FastItemAdapter();
        rv_clinic_unavailable_days.itemAnimator = SlideDownAlphaAnimator()
        rv_clinic_unavailable_days.layoutManager =
            LinearLayoutManager(this@ClinicActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_clinic_unavailable_days.addItemDecoration(HorizontalSpaceItemDecoration(20))
        rv_clinic_unavailable_days.adapter = unavailableDateAdapterItem;

        this.fetchAppointments(clinic);
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

    // Fetch Appointments
    private lateinit var unavailableRef: DatabaseReference;
    private fun fetchAppointments(clinic: Clinic) {
        // clinic data listener
        unavailableRef = Firebase.database.reference.child("unavailable_date");
        val clinicsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val unavailableDates : MutableList<UnavailableDate?> = mutableListOf();
                    // Fetch user data from firebase
                    for (postSnapshot in dataSnapshot.children) {
                        val fetchedDate: UnavailableDate? = postSnapshot.getValue<UnavailableDate>()!!;
                        if(fetchedDate!!.clinicKey == clinic.id){
                            unavailableDates.add(fetchedDate);
                        }
                    }
                if(!unavailableDates.isEmpty()) {
                    initUnavailableItems(unavailableDates);
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        unavailableRef.addValueEventListener(clinicsListener)
    }
}