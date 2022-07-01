package com.xub.lakad.presentation.common.libs

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration @JvmOverloads constructor(
    private val mVerticalSpaceHeight: Int,
    private val mSpaceLastItemBelow: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1 ||
            mSpaceLastItemBelow
        ) {
            outRect.right = mVerticalSpaceHeight
        }
    }
}
