package cf.untitled.salend.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cf.untitled.salend.R
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.FragmentMapBinding
import cf.untitled.salend.model.StoreArray
import cf.untitled.salend.model.StoreData
import cf.untitled.salend.retrofit.RetrofitClass
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


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
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    var nearbyStoreList = ArrayList<StoreData>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mCalloutBalloon : View
    private lateinit var listener : MarkerEventListener
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentMapBinding.inflate(layoutInflater)
        mCalloutBalloon = layoutInflater.inflate(R.layout.item_map_info, null)
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        listener = this.context?.let { MarkerEventListener(it) }!!
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
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater, mCalloutBalloon))
        mapView.setPOIItemEventListener(listener)

        RetrofitClass.service.getNearbyStorePage().enqueue(object :
            Callback<StoreArray> {
            override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    var result: StoreArray? = response.body()
                    result?.stores?.let { nearbyStoreList.addAll(it) }

                    for (i in 0 until nearbyStoreList.size) {
                        nearbyStoreList[i]._id?.let {
                            nearbyStoreList[i].s_lat?.let {it1 ->
                                nearbyStoreList[i].s_lng?.let { it2 ->
                                    createMapMarker(
                                        it,
                                        it1,
                                        it2
                                    )
                                }
                            }
                        }
                    }
                    Log.d("grusie", "$result")

                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("retrofit", "${response.code()}")
                    Log.d("retrofit", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<StoreArray>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

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
        } else {
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOff
        }
    }

    private fun createMapMarker(id:String, latitude:Double, longitude:Double) {
        val marker = MapPOIItem()
        marker.apply {
            for (i in 0 until nearbyStoreList.size){
                if(nearbyStoreList[i]._id == id) {
                    nearbyStoreList[i].apply {
                        itemName = s_name
                        userObject = _id

                        Glide.with(this@MapFragment)
                            .asBitmap().load(s_image)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .listener(object : RequestListener<Bitmap?> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Bitmap?>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Bitmap?,
                                    model: Any?,
                                    target: Target<Bitmap?>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    if (resource != null) {
                                        customImageBitmap = resource
                                    }
                                    return false
                                }
                            }
                            ).submit()
                    }
                }
            }
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)   // 좌표
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        mapView.addPOIItem(marker)
    }

    class CustomBalloonAdapter(inflater: LayoutInflater, mCalloutBalloon: View): CalloutBalloonAdapter {
        private val mCalloutBalloon2 = mCalloutBalloon
        val name: TextView = mCalloutBalloon2.findViewById(R.id.map_store_name)
        private val img: ImageView = mCalloutBalloon2.findViewById(R.id.map_store_image)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName
            if (poiItem?.customImageBitmap != null) {
                img.setImageBitmap(poiItem?.customImageBitmap)
            }

            return mCalloutBalloon2
        }
        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            return mCalloutBalloon2
        }
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

class MarkerEventListener(val context: Context) : MapView.POIItemEventListener {
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        return
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        return
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        var intent = Intent(context, StoreChoiceActivity::class.java)
        intent.putExtra("id", p1?.userObject.toString())
        ContextCompat.startActivity(context, intent, null)
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        TODO("Not yet implemented")
    }

}
