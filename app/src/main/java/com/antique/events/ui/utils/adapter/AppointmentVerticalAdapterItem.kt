package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.data.model.Appointment
import kotlinx.android.synthetic.main.item_appointment.view.item_datetime
import kotlinx.android.synthetic.main.item_appointment.view.item_department
import kotlinx.android.synthetic.main.item_appointment.view.item_participants
import kotlinx.android.synthetic.main.item_appointment.view.item_description
import kotlinx.android.synthetic.main.item_appointment.view.item_title
import kotlinx.android.synthetic.main.item_appointment_clinic_vertical.view.btn_cancel_appointment

class AppointmentVerticalAdapterItem(appointment: Appointment, val hasRemove: Boolean, val cancelAction: (appointment: Appointment) -> Unit) :
    FastAdapterItem<Appointment, AppointmentVerticalAdapterItem, AppointmentVerticalAdapterItem.ViewHolder>(appointment) {

    override fun getType(): Int {
        return R.id.appointment_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v);
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_appointment_vertical
    }

    override fun bindView(holder: AppointmentVerticalAdapterItem.ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.item_title.text = model.clinicName;
        holder.item_remaining_time.text = model.remainingTime;
        holder.item_doctor_name.text = model.doctor;
        holder.item_position.text = model.position;
        holder.item_datetime.text = model.date.toString();

        if(hasRemove) {
            holder.item_remove_button.visibility = View.VISIBLE
            holder.item_remove_button.setOnClickListener {
                cancelAction(model);
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_title = itemView.item_title as TextView;
        val item_remaining_time = itemView.item_description as TextView;
        val item_doctor_name = itemView.item_department as TextView;
        val item_position = itemView.item_participants as TextView;
        val item_datetime = itemView.item_datetime as TextView;

        val item_remove_button = itemView.btn_cancel_appointment
    }
}