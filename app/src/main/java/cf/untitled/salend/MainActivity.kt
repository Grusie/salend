package cf.untitled.salend

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cf.untitled.salend.adapter.MainFragmentStatePagerAdapter
import cf.untitled.salend.databinding.ActivityMainBinding
import cf.untitled.salend.databinding.BottomNavigationTabBinding


//메인 액티비티, 뷰페이저 및 퍼미션 체크 등을 구현
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val PERMISSION_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            configureBottomNavigation()
        else
            requestRuntimePermissions()
    }


    private fun configureBottomNavigation() {
        val tabButtonBinding = BottomNavigationTabBinding.inflate(layoutInflater)
        binding.apply {
            vpAcMainFragPager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)
            tlAcMainBottomMenu.setupWithViewPager(vpAcMainFragPager)
            vpAcMainFragPager.offscreenPageLimit = 5
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    throw RuntimeException("Empty permission result")
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureBottomNavigation()
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("위치 권한 필요").setMessage("현재 위치를 받아오려면 위치 권한을 켜주세요")
                        .setPositiveButton("권한설정하기") { _, _ ->
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                                startActivity(intent)
                            }
                        }.setNegativeButton("취소") { _, _ ->
                            Toast.makeText(this, "위치 권한을 설정하지 않았습니다.", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }.create().show()
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
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