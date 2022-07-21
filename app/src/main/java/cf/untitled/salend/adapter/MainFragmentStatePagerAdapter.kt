package cf.untitled.salend.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import cf.untitled.salend.fragment.*


class MainFragmentStatePagerAdapter(fm : FragmentManager, val fragmentCount : Int) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int  = fragmentCount

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return LikeFragment()
            2 -> return MapFragment()
            3 -> return PayListFragment()
            4 -> return MyPageFragment()
            else -> return HomeFragment()
        }
    }
    
}