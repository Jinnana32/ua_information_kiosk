package com.antique.events.ui.utils.components

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.antique.events.R

class RegistrationMapSheetDialog : BottomSheetDialogFragment() {

    companion object {
        fun newInstance()
                : RegistrationMapSheetDialog {
            val dialog = RegistrationMapSheetDialog()
            val bundle = Bundle()
            return dialog
        }
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

        val contentView = View.inflate(context, R.layout.barcode_bottom_sheet, null)
        dialog.setContentView(contentView)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT)

        return dialog;
    }

}