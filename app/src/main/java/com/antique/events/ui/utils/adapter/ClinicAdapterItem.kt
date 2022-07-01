package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.data.model.Clinic
import kotlinx.android.synthetic.main.item_clinic.view.tv_item_address
import kotlinx.android.synthetic.main.item_clinic.view.tv_item_position
import kotlinx.android.synthetic.main.item_clinic.view.tv_item_title

class ClinicAdapterItem(clinic: Clinic) :
    FastAdapterItem<Clinic, ClinicAdapterItem, ClinicAdapterItem.ViewHolder>(clinic) {

    override fun getType(): Int {
        return R.id.clinic_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v);
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_clinic
    }

    override fun bindView(holder: ClinicAdapterItem.ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.tv_item_title.text = model.name;
        holder.tv_item_position.text = model.specialization;
        holder.tv_item_address.text = model.address;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_item_title = itemView.tv_item_title as TextView;
        val tv_item_position = itemView.tv_item_position as TextView;
        val tv_item_address = itemView.tv_item_address as TextView;
    }
}