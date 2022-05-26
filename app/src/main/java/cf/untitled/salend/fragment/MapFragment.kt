package cf.untitled.salend.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.DecimalFormat
import kotlin.math.log


// TODO: Rename parameter arguments, choose names that match
// the cf.untitled.salend.fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [mapFragment.newInstance] factory method to
 * create an instance of this cf.untitled.salend.fragment.
 */
class MapFragment : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var binding: FragmentMapBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this cf.untitled.salend.fragment
        mapView = MapView(this.context)
        val mapViewContainer = binding.mapView as ViewGroup
        mapViewContainer.addView(mapView)
        setCurrentLocationTrackingMode(true)

        var latitude = MyApplication.sharedPref.getString("latitude","0")!!.toDouble()
        var longitude = MyApplication.sharedPref.getString("longitude","0")!!.toDouble()
        createMapMarker(latitude, longitude)
        binding.targetBtn.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            mapView.setMapCenterPoint(
                                MapPoint.mapPointWithGeoCoord(
                                    location.latitude,
                                    location.longitude
                                ), true
                            )
                            mapView.setZoomLevel(3, true)
                        }
                    }
                if (mapView.currentLocationTrackingMode == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
                    setCurrentLocationTrackingMode(false)
                } else {
                    setCurrentLocationTrackingMode(true)
                }
            } else {
            }
        }
        return binding.root
    }

    private fun setCurrentLocationTrackingMode(tracking: Boolean) {
        if (tracking) {
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            binding.targetBtn.setColorFilter(Color.parseColor("#0000FF"))
        } else {
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOff
            binding.targetBtn.setColorFilter(Color.parseColor("#FF000000"))
        }
    }

    private fun createMapMarker(latitude:Double, longitude:Double) {
        Toast.makeText(requireContext(),"createMapMarker : $latitude, $longitude", Toast.LENGTH_SHORT).show()
        val marker = MapPOIItem()
        Log.d("grusie","$latitude, $longitude")
        marker.apply {
            itemName = "qweazx"   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)   // 좌표
            marker.markerType = MapPOIItem.MarkerType.BluePin
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        mapView.addPOIItem(marker)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this cf.untitled.salend.fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of cf.untitled.salend.fragment mapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}