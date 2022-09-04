package cf.untitled.salend.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.LocationSelectActivity
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.SearchActivity
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.databinding.FragmentHomeBinding
import cf.untitled.salend.model.ProductArray
import cf.untitled.salend.model.ProductData
import cf.untitled.salend.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var activityResultLauncher: ActivityResultLauncher<Intent>     //location result값을 받아오기 위해 사용하는 Intent

class HomeFragment : Fragment() {       //메인 Fragment 앱 처음 실행 시 띄워지는 Fragment
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MyApplication.sharedPref =
            requireActivity().getSharedPreferences("location", Context.MODE_PRIVATE)
        MyApplication.edit =
            MyApplication.sharedPref.edit()        //Location을 저장할 SharedPreference 선언

        binding = FragmentHomeBinding.inflate(layoutInflater)
        val textViewList: Array<TextView> = arrayOf(        //카테고리 버튼 배열
            binding.categoryText1,
            binding.categoryText2,
            binding.categoryText3,
            binding.categoryText4,
            binding.categoryText5,
            binding.categoryText6,
            binding.categoryText7,
            binding.categoryText8
        )
        val btnList: Array<CardView> = arrayOf(        //카테고리 버튼 배열
            binding.categoryBtn0,
            binding.categoryBtn1,
            binding.categoryBtn2,
            binding.categoryBtn3,
            binding.categoryBtn4,
            binding.categoryBtn5,
            binding.categoryBtn6,
            binding.categoryBtn7
        )
        val stringArray = resources.getStringArray(R.array.category)

        for (i in stringArray.indices) {
            textViewList[i].text = stringArray[i]
        }

        binding.locationInfo.text = MyApplication.sharedPref.getString("location", "지역")
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {      //액티비티 결과 콜백 지정
                if (it.resultCode == Activity.RESULT_OK) {
                    binding.locationInfo.text = it.data?.getStringExtra("locationResult")
                    initRecyclerView()
                }
            }
        binding.locationTargetBtn.setOnClickListener {     //지역 버튼 클릭 시 LocationSelectActivity 실행
            val intent = Intent(this.context, LocationSelectActivity::class.java)
            intent.putExtra("location", binding.locationInfo.text.toString())
            activityResultLauncher.launch(intent)
        }

        binding.searchView.setOnClickListener {
            val intent = Intent(this.context, SearchActivity::class.java)
            startActivity(intent)
        }

        for (i: Int in btnList.indices) {
            btnList[i].setOnClickListener {
                val intent = Intent(this.context, SearchActivity::class.java)
                intent.putExtra("categoryName", "$i")
                startActivity(intent)
            }
        }

        return initRecyclerView()
    }

    private fun initRecyclerView(): View {     //retrofit통신으로, ProductArray형태를 받아 옴
        val nearbyProductList = ArrayList<ProductData>()
        val endTimeProductList = ArrayList<ProductData>()
        val latitude = MyApplication.sharedPref.getString("latitude", "0")
        val longitude = MyApplication.sharedPref.getString("longitude", "0")
        RetrofitClass.service.getProductArrayPage("${latitude},${longitude}")
            .enqueue(object : Callback<ProductArray> {
                override fun onResponse(
                    call: Call<ProductArray>,
                    response: Response<ProductArray>
                ) {
                    if (response.isSuccessful) {

                        // 정상적으로 통신이 성공된 경우
                        val result: ProductArray? = response.body()
                        result?.near_by?.let { nearbyProductList.addAll(it) }
                        result?.end_time?.let { endTimeProductList.addAll(it.reversed()) }
                        setRecyclerData(nearbyProductList, endTimeProductList)

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        Log.d("retrofit", "${response.code()}")
                        Log.d("retrofit", "onResponse 실패")
                    }
                }

                override fun onFailure(call: Call<ProductArray>, t: Throwable) {
                    // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                    Log.d("retrofit", "onFailure 에러: " + t.message.toString())
                }
            })
        return binding.root
    }

    private fun setRecyclerData(        //리사이클러 뷰 데이터 지정
        nearByProductList: ArrayList<ProductData>,
        endTimeProductList: ArrayList<ProductData>
    ) {
        binding.nearbySaleRecyclerview.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = NearbySaleRecyclerAdapter(nearByProductList)
        }
        binding.deadlineSaleRecyclerview.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = NearbySaleRecyclerAdapter(endTimeProductList)
        }

    }
}