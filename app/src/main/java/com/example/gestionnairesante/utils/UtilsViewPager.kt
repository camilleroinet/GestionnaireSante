@file: JvmName("UtilsViewPager")
@file: JvmMultifileClass


package com.example.gestionnairesante.utils


import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.adapter.ViewPagerAdapter
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayout

fun Fragment.configViewPager(
    viewPager: ViewPager,
    arrayFrag: ArrayList<Fragment>,
    arrayTab: ArrayList<Int>,
    tablayout: TabLayout
){
    viewPager.apply {
        viewPager.adapter = ViewPagerAdapter(
            arrayFrag, arrayTab,
            childFragmentManager, tablayout.tabCount, context)
    }
    tablayout.setupWithViewPager(viewPager, true)
    viewPager.setPageTransformer(true, ZoomOutPageTransformer())
}