package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.data.model.Event
import kotlinx.android.synthetic.main.item_appointment.view.item_datetime
import kotlinx.android.synthetic.main.item_appointment.view.item_department
import kotlinx.android.synthetic.main.item_appointment.view.item_participants
import kotlinx.android.synthetic.main.item_appointment.view.item_description
import kotlinx.android.synthetic.main.item_appointment.view.item_title

class EventAdapterItem(event: Event) : FastAdapterItem<Event, EventAdapterItem, EventAdapterItem.ViewHolder>(event) {

        override fun getType(): Int {
            return R.id.event_item
        }

        override fun getViewHolder(v: View): ViewHolder {
            return ViewHolder(v);
        }

        override fun getLayoutRes(): Int {
            return R.layout.item_event_vertical
        }

        override fun bindView(holder: EventAdapterItem.ViewHolder, payloads: MutableList<Any>) {
            super.bindView(holder, payloads)
            holder.item_title.text = model.title;
            holder.item_description.text = model.description;
            holder.item_department.text = model.department;
            holder.item_participants.text = "Participants ${model.participants.size}"
            holder.item_datetime.text = "June 11, 5:30pm - June 12, 6:00pm"
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val item_title = itemView.item_title as TextView;
            val item_description = itemView.item_description as TextView;
            val item_department = itemView.item_department as TextView;
            val item_participants = itemView.item_participants as TextView;
            val item_datetime = itemView.item_datetime as TextView;
        }
}
