package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.ui.utils.helper.DateUtil
import kotlinx.android.synthetic.main.item_appointment_taken.view.*

class AppointmentTakenAdapterItem(date: String) :
    FastAdapterItem<String, AppointmentTakenAdapterItem, AppointmentTakenAdapterItem.ViewHolder>(date) {

    override fun getType(): Int {
        return R.id.appointment_taken_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v);
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_appointment_taken
    }

    override fun bindView(holder: AppointmentTakenAdapterItem.ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        val timeSlot : String = DateUtil.parseDate(model, DateUtil.GOOD_FORMAT_DATE_2, DateUtil.GOOD_FORMAT_TIME_HM);
        holder.item_time_slot.text = timeSlot;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_time_slot = itemView.item_time_slot as TextView;
    }
}