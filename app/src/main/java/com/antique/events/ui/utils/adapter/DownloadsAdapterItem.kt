package com.antique.events.ui.utils.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antique.events.R
import com.antique.events.data.model.DownloadData
import com.antique.events.data.model.Event
import kotlinx.android.synthetic.main.item_appointment.view.item_datetime
import kotlinx.android.synthetic.main.item_appointment.view.item_department
import kotlinx.android.synthetic.main.item_appointment.view.item_participants
import kotlinx.android.synthetic.main.item_appointment.view.item_description
import kotlinx.android.synthetic.main.item_appointment.view.item_title
import kotlinx.android.synthetic.main.item_download.view.download_desc
import kotlinx.android.synthetic.main.item_download.view.download_text

class DownloadsAdapterItem(download: DownloadData) : FastAdapterItem<DownloadData, DownloadsAdapterItem, DownloadsAdapterItem.ViewHolder>(download) {

        override fun getType(): Int {
            return R.id.event_item
        }

        override fun getViewHolder(v: View): ViewHolder {
            return ViewHolder(v);
        }

        override fun getLayoutRes(): Int {
            return R.layout.item_download
        }

        override fun bindView(holder: DownloadsAdapterItem.ViewHolder, payloads: MutableList<Any>) {
            super.bindView(holder, payloads)
            holder.download_text.text = model.fileName
            holder.download_desc.text = "Description: ${model.description}"
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val download_text = itemView.download_text as TextView;
            val download_desc = itemView.download_desc as TextView;
        }
}
