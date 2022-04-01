package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import cf.untitled.salend.adapter.MainFragmentStatePagerAdapter
import cf.untitled.salend.databinding.ActivityMainBinding
import cf.untitled.salend.databinding.BottomNavigationTabBinding

lateinit var binding:ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureBottomNavigation()
    }

    private fun configureBottomNavigation(){
        val tabButtonBinding = BottomNavigationTabBinding.inflate(layoutInflater)
        binding.apply {
            vpAcMainFragPager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)
            tlAcMainBottomMenu.setupWithViewPager(vpAcMainFragPager)
            tlAcMainBottomMenu.getTabAt(0)!!.customView = tabButtonBinding.btnBottomNaviHomeTab
            tlAcMainBottomMenu.getTabAt(1)!!.customView = tabButtonBinding.btnBottomNaviLikeTab
            tlAcMainBottomMenu.getTabAt(2)!!.customView = tabButtonBinding.btnBottomNaviMapTab
            tlAcMainBottomMenu.getTabAt(3)!!.customView = tabButtonBinding.btnBottomNaviPayListTab
            tlAcMainBottomMenu.getTabAt(4)!!.customView = tabButtonBinding.btnBottomNaviMyPageTab

        }
    }
}