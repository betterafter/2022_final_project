package com.kuroutine.kulture.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPager2Adapter(parent: Fragment, list: ArrayList<Fragment>) :
    FragmentStateAdapter(parent) {
    private val mFragments: ArrayList<Fragment>

    init {
        this.mFragments = list
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }
}