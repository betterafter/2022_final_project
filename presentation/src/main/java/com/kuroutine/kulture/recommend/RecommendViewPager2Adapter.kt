package com.kuroutine.kulture.recommend

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecommendViewPager2Adapter(parent: Fragment, list: ArrayList<Fragment>) :
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