package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.data.model.Appointment
import kotlinx.android.synthetic.main.item_appointment.view.item_datetime
import kotlinx.android.synthetic.main.item_appointment_clinic_vertical.view.*

class AppointmentClinicVerticalAdapterItem(appointment: Appointment, val hasRemove: Boolean, val cancelAction: (appointment: Appointment) -> Unit) :
    FastAdapterItem<Appointment, AppointmentClinicVerticalAdapterItem, AppointmentClinicVerticalAdapterItem.ViewHolder>(appointment) {

    override fun getType(): Int {
        return R.id.appointment_clinic_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v);
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_appointment_clinic_vertical
    }

    override fun bindView(holder: AppointmentClinicVerticalAdapterItem.ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.item_patient_name.text = model.patientName;
        holder.item_patient_address.text = model.patientAddress;
        holder.item_datetime.text = model.date.toString();
        holder.item_status.text = model.toString();
        holder.item_status.setTextColor(model.getStatusColor());
        holder.item_contactNumber.text = model.patientContactNumber

        if(hasRemove!!) {
            holder.item_remove_button.visibility = View.VISIBLE
            holder.item_remove_button.setOnClickListener {
                cancelAction(model);
            }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_patient_name = itemView.item_patient_name as TextView;
        val item_patient_address = itemView.item_patient_address as TextView;
        val item_datetime = itemView.item_datetime as TextView;
        val item_status = itemView.item_status as TextView;
        val item_contactNumber = itemView.item_contact_number as TextView;
        val item_remove_button = itemView.btn_cancel_appointment
    }

}