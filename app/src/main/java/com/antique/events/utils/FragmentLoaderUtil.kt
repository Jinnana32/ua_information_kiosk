package com.antique.events.utils

import androidx.fragment.app.FragmentManager
import com.itsgmobility.hrbenefits.ui.placeholders.EmptyFragment
import com.itsgmobility.hrbenefits.ui.placeholders.LoadingFragment

object FragmentLoaderUtil {

    fun loadSection(fm: FragmentManager, layout: Int) {
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(layout, LoadingFragment())
        fragmentTransaction.commitNowAllowingStateLoss()
    }

    fun emptySection(fm: FragmentManager, layout: Int, message: String? = null, title: String? = null){
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(layout, EmptyFragment.newInstance(message, title))
        fragmentTransaction.commitNowAllowingStateLoss()
    }

}