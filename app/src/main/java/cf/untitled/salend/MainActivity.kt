package cf.untitled.salend

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import cf.untitled.salend.adapter.MainFragmentStatePagerAdapter
import cf.untitled.salend.databinding.ActivityMainBinding
import cf.untitled.salend.databinding.BottomNavigationTabBinding
import com.kakao.sdk.common.util.Utility
import java.security.MessageDigest
import java.util.*



class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    val PERMISSION_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureBottomNavigation()
        requestRuntimePermissions()
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

    private fun requestRuntimePermissions() {       //지도 permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    throw RuntimeException("Empty permission result")
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            PERMISSION_REQUEST_CODE
                        )
                    } else {
                    }
                }
            }
        }
    }

}