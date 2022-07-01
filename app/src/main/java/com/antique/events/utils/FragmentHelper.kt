package com.antique.events.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentHelper {

    fun setupFragment(fragmentManager: FragmentManager, baseFragment: Fragment, frameLayoutId: Int) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(frameLayoutId, baseFragment)
        fragmentTransaction.commitNowAllowingStateLoss()
    }

}
