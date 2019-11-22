package com.rk.jdbc.postman.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter




/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
      /*  if(position >= fragmentList.size){
//            intialize
            var fragment = PlaceholderFragment.newInstance(position + 1)
            fragmentList.add(fragment)
            return fragment
        }else{
//            check in list
            return fragmentList.get(position)
        }*/


        return fragmentList.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun removeFragment(position: Int) {
        fragmentList.removeAt(position)
        fragmentTitleList.removeAt(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList.get(position)
//        return super.getPageTitle(position);
    }

    override fun getCount(): Int {
        // Show dynamic total pages.
        return fragmentList.size
    }

}