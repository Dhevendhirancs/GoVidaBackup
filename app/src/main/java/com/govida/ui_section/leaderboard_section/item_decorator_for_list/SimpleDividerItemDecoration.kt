/**
 * @Class : SimpleDividerItemDecoration
 * @Usage : Used to provide simple item decorator as line between views
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.item_decorator_for_list

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.govida.R
import com.govida.ui_section.leaderboard_section.ActivityLeaderboard

class SimpleDividerItemDecoration(activityNotification: ActivityLeaderboard) : RecyclerView.ItemDecoration() {
    private var mDivider: Drawable = activityNotification.resources.getDrawable(R.drawable.points_separator)


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