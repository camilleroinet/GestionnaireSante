package com.example.gestionnairesante.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerChartsAdapter(
    var arrayFrag: ArrayList<Fragment>,
    var arrayTab: ArrayList<Int>,
    fm: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return arrayFrag.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            position -> arrayFrag[position]
            else -> arrayFrag[0]
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            position -> context.getString(arrayTab[position])
            else -> context.getString(arrayTab[0])
        }
    }
}