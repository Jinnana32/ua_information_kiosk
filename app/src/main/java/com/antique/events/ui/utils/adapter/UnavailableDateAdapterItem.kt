package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.data.model.UnavailableDate
import com.antique.events.ui.utils.helper.DateUtil
import kotlinx.android.synthetic.main.item_unavailable_date.view.*

class UnavailableDateAdapterItem(date: UnavailableDate) :
    FastAdapterItem<UnavailableDate, UnavailableDateAdapterItem, UnavailableDateAdapterItem.ViewHolder>(date) {

    override fun getType(): Int {
        return R.id.unavailable_date_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v);
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_unavailable_date
    }

    override fun bindView(holder: UnavailableDateAdapterItem.ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        val date : String = DateUtil.parseDate(model.date, DateUtil.GOOD_FORMAT_DATE,DateUtil.GOOD_FORMAT_DATE);
        var fullTime = date;
        fullTime = if(model.notWholeDay) {
            "${fullTime} ${model.startTime} - ${model.endTime}";
        } else {
            fullTime;
        }
        holder.item_date.text = fullTime;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_date = itemView.item_date as TextView;
    }
}