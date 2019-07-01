package com.govida.ui_section.home_section

import java.util.*

class HomeTabAdapter(fm: androidx.fragment.app.FragmentManager?, fragments: ArrayList<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private val data: List<androidx.fragment.app.Fragment>? = fragments

    override fun getItem(p0: Int): androidx.fragment.app.Fragment {
        return data!!.get(p0)
    }

    override fun getCount(): Int {
        return data!!.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE;
    }
}