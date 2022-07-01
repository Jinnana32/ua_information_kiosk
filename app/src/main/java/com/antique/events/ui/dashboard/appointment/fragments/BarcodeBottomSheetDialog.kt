package com.antique.events.ui.dashboard.appointment.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.antique.events.R
import com.antique.events.ui.utils.components.UBButton
import kotlinx.android.synthetic.main.barcode_bottom_sheet.view.*

class BarcodeBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        const val DIALOG_CONTENT = "DIALOG_CONTENT"
        fun newInstance(content: String)
                : BarcodeBottomSheetDialog {
            val dialog = BarcodeBottomSheetDialog()
            val bundle = Bundle()
            bundle.putString(DIALOG_CONTENT, content)
            dialog.arguments = bundle
            return dialog
        }
    }

    private lateinit var barcodeImageView: ImageView;
    private lateinit var btn_dismiss: UBButton;
    private lateinit var onDialogClose: () -> Unit?

    fun withOnDialogClose(action: () -> Unit) : BarcodeBottomSheetDialog {
        onDialogClose = action;
        return this;
    }

    fun withIsDismissable( isDismissable: Boolean ) : BarcodeBottomSheetDialog {
        this.isCancelable = isDismissable;
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

        val contentView = View.inflate(context, R.layout.barcode_bottom_sheet, null)
        dialog.setContentView(contentView)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT)

        barcodeImageView = contentView.iv_barcode;
        btn_dismiss = contentView.scan_button;

        // Dismiss button
        btn_dismiss.setOnClickListener {
            onDialogClose();
        }

        val barcodeContent = this.requireArguments().getString(DIALOG_CONTENT);
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap =
                barcodeEncoder.encodeBitmap(barcodeContent, BarcodeFormat.QR_CODE, 250, 250)
            barcodeImageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Android debug", e.message!!);
        }

        return dialog;
    }

}