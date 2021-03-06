package cf.untitled.salend

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import android.view.View
import android.widget.Toast
import cf.untitled.salend.adapter.MainFragmentStatePagerAdapter
import cf.untitled.salend.databinding.ActivityMainBinding
import cf.untitled.salend.databinding.BottomNavigationTabBinding
import cf.untitled.salend.databinding.FragmentHomeBinding
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.security.MessageDigest
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val PERMISSION_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
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

    private fun requestRuntimePermissions() {       //?????? permission
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
                    builder.setTitle("?????? ?????? ??????").setMessage("?????? ????????? ??????????????? ?????? ????????? ????????????")
                        .setPositiveButton("??????????????????") { _, _ ->
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                startActivity(intent)
                            }
                        }.setNegativeButton("??????") { _, _ ->
                            Toast.makeText(this, "?????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT)
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