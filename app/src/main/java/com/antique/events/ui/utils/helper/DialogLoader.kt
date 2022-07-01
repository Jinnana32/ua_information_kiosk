package com.antique.events.ui.utils.helper

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.antique.events.R
import kotlinx.android.synthetic.main.layout_loading.view.*

object DialogLoader {

    fun createProgressDialog(
        context: Context?,
        message: String? = "Loading..."
    ): Dialog? {
        val layoutLoading = LayoutInflater.from(context).inflate(
            R.layout.layout_loading,
            null, false
        ) as LinearLayout
        val tvMessage = layoutLoading.tv_message
        tvMessage.setText(message)
        val dialog = Dialog(context!!)
        dialog.setCancelable(false)
        dialog.setContentView(layoutLoading)
        return dialog
    }

}