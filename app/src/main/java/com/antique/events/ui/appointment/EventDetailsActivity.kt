package com.antique.events.ui.appointment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.antique.events.R
import com.antique.events.RegisterActivity
import com.antique.events.data.model.Event
import com.antique.events.data.model.RegisterOrActivityResponse
import com.antique.events.data.model.RegisterOrJoinActivityParams
import com.antique.events.data.services.ApiRequester
import com.antique.events.data.services.LocalStorageService
import kotlinx.android.synthetic.main.activity_appointment_details.*
import kotlinx.android.synthetic.main.activity_appointment_form.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailsActivity : AppCompatActivity() {

    companion object {
        private val EVENT_MODEL = "EVENT_MODEL";

        fun getIntent(
            context: Context,
            event: Event? = null
        ): Intent {
            return Intent(context, EventDetailsActivity::class.java)
                .putExtra(EVENT_MODEL, event);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)

        val toolbar: Toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar !== null) {
            supportActionBar!!.title = "Appointment Details";
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener {
                finish();
            }
        }

        val event =
            this@EventDetailsActivity.intent.extras!!.getParcelable<Event>(
                EVENT_MODEL
            );

        val user = LocalStorageService.getUser();

        var registerOrJoinActivityParams = RegisterOrJoinActivityParams(
            email = user.email,
            fname = user.fname,
            lname = user.lname,
            mname = user.mname,
            type = user.type
        )

        // if not in register show register
        var isRegistered = false;
        var isPartipant = false;
        val registeredUsers = event!!.registeredUsers;
        val participants = event!!.participants;
        registeredUsers.forEach {
            if(user.email == it.email && !isRegistered){
                isRegistered = true;
            }
        }
        // if registered but not participant show join
        if(isRegistered) {
            participants.forEach {
                if(user.email == it.email && !isPartipant){
                    isPartipant = true;
                }
            }
        }

        if(!isRegistered){
            btn_register_appointment.visibility = View.VISIBLE;
        }

        if(isPartipant) {
            btn_join_appointment.visibility = View.VISIBLE;
        }

        btn_register_appointment.setOnClickListener {

            ApiRequester.registerActivity(registerOrJoinActivityParams, event.id).enqueue(object : Callback<RegisterOrActivityResponse?> {
                override fun onResponse(
                    call: Call<RegisterOrActivityResponse?>,
                    response: Response<RegisterOrActivityResponse?>
                ) {
                    btn_join_appointment.visibility = View.VISIBLE;
                    btn_register_appointment.visibility = View.GONE;
                    Toast.makeText(
                        this@EventDetailsActivity,
                        "You are now registered to this event",
                        Toast.LENGTH_LONG
                    ).show();
                }

                override fun onFailure(call: Call<RegisterOrActivityResponse?>, t: Throwable) {
                    Toast.makeText(
                        this@EventDetailsActivity,
                        "Problem with connection please logout and try again",
                        Toast.LENGTH_LONG
                    ).show();
                }
            })
        }

        btn_join_appointment.setOnClickListener {
            ApiRequester.joinActivity(registerOrJoinActivityParams, event.id).enqueue(object : Callback<RegisterOrActivityResponse?> {
                override fun onResponse(
                    call: Call<RegisterOrActivityResponse?>,
                    response: Response<RegisterOrActivityResponse?>
                ) {
                    btn_join_appointment.visibility = View.GONE;
                    btn_register_appointment.visibility = View.GONE;
                    Toast.makeText(
                        this@EventDetailsActivity,
                        "You are now a participant of this event",
                        Toast.LENGTH_LONG
                    ).show();
                }

                override fun onFailure(call: Call<RegisterOrActivityResponse?>, t: Throwable) {
                    Toast.makeText(
                        this@EventDetailsActivity,
                        "Problem with connection please logout and try again",
                        Toast.LENGTH_LONG
                    ).show();
                }
            })
        }

        initEvent(event);
    }

    private fun initEvent(event: Event) {
        event_name.text = event.title;
        event_description.text = event.description;
        event_department.text = event.department;
        event_participants.text = event.participants.map { "${it.fname} ${it.lname}" }.joinToString(", ")
        event_registered_users.text = event.registeredUsers.map { "${it.fname} ${it.lname}" }.joinToString(" ")
        event_datetime.text = "${event.startDateTime} - ${event.endDateTime}"
    }

}