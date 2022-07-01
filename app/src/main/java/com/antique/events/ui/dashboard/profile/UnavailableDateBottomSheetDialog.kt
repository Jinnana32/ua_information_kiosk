package com.antique.events.ui.dashboard.profile

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.antique.events.R
import com.antique.events.ui.utils.components.UBButton
import com.antique.events.ui.utils.components.UBEditText
import com.antique.events.ui.utils.helper.DatePickerHelper
import com.antique.events.ui.utils.helper.DateUtil
import kotlinx.android.synthetic.main.unavailable_date.view.*

class UnavailableDateBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        const val DIALOG_CONTENT = "DIALOG_CONTENT"
        fun newInstance()
                : UnavailableDateBottomSheetDialog {
            val dialog = UnavailableDateBottomSheetDialog()
            return dialog
        }
    }

    private val APPOINTMENT_UNAVAILABLE_DATE_TAG = "APPOINTMENT_UNAVAILABLE_DATE_TAG";
    private val START_TIME_TAG = "START_TIME_TAG";
    private val END_TIME_TAG = "END_TIME_TAG";

    private lateinit var et_date: UBEditText;
    private lateinit var startTime: UBEditText;
    private lateinit var endTime: UBEditText;
    private lateinit var cbWholeDay: AppCompatCheckBox;
    private lateinit var layoutTime: LinearLayoutCompat;
    private lateinit var btn_dismiss: UBButton;
    private lateinit var onDialogClose: (date: String, notWholeDay: Boolean, startTime: String, endTime: String) -> Unit?

    fun withOnSubmit(action: (date: String, notWholeDay: Boolean, startTime: String, endTime: String) -> Unit?) : UnavailableDateBottomSheetDialog {
        onDialogClose = action;
        return this;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialog1 ->
            val d = dialog1 as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.from(bottomSheet).setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dismiss()
                        }
                    }
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
        }

        val contentView = View.inflate(context, R.layout.unavailable_date, null)
        dialog.setContentView(contentView)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT)

        btn_dismiss = contentView.btn_submit;
        et_date = contentView.et_date_pick;
        startTime = contentView.et_start_time;
        endTime = contentView.et_end_time
        cbWholeDay = contentView.cb_wholeday;
        layoutTime = contentView.layout_time;

        btn_dismiss.setOnClickListener {
            onDialogClose(et_date.text.toString(), cbWholeDay.isChecked, startTime.text.toString(), endTime.text.toString());
        }

        cbWholeDay.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                layoutTime.visibility = View.VISIBLE
            }else{
                layoutTime.visibility = View.GONE
            }
        }

        startTime.setOnClickListener {
            DatePickerHelper.showTimePicker(childFragmentManager, START_TIME_TAG) {
                val pickedTime = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_TIME_HM);
                startTime.setText(pickedTime);
            }
        }

        endTime.setOnClickListener {
            DatePickerHelper.showTimePicker(childFragmentManager, END_TIME_TAG) {
                val pickedTime = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_TIME_HM);
                endTime.setText(pickedTime);
            }
        }

        et_date.setOnClickListener {
            DatePickerHelper.showDatePicker(childFragmentManager, APPOINTMENT_UNAVAILABLE_DATE_TAG) {
                val datePicked = DateUtil.formatDate(it.time, DateUtil.GOOD_FORMAT_DATE);
                et_date.setText(datePicked);
            }
        }

        return dialog;
    }

}