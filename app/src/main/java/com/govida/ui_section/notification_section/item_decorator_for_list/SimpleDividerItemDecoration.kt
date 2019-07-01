package com.govida.ui_section.notification_section.item_decorator_for_list

import androidx.recyclerview.widget.RecyclerView
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.govida.R
import com.govida.ui_section.notification_section.ActivityNotification

class SimpleDividerItemDecoration(val activityNotification: ActivityNotification) : RecyclerView.ItemDecoration() {
    var mDivider: Drawable = activityNotification.resources.getDrawable(R.drawable.points_separator)


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}