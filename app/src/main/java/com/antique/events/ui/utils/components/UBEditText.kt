package com.antique.events.ui.utils.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.antique.events.R

class UBEditText : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs,
        R.attr.ubEditTextStyle
    ) {
    }
}