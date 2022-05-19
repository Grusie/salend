package cf.untitled.salend.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.KategorieActivity
import cf.untitled.salend.LocationSelectActivity
import cf.untitled.salend.MyApplication
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.databinding.FragmentHomeBinding
import cf.untitled.salend.model.ProductArray
import cf.untitled.salend.model.ProductData
import cf.untitled.salend.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the cf.untitled.salend.fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var activityResultLauncher: ActivityResultLauncher<Intent>     //location result값을 받아오기 위해 사용하는 Intent

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        MyApplication.sharedPref = requireActivity().getSharedPreferences("location", Context.MODE_PRIVATE)   
        MyApplication.edit = MyApplication.sharedPref.edit()        //Location을 저장할 SharedPreference 선언

        binding = FragmentHomeBinding.inflate(layoutInflater)
        val btnList: Array<cf.untitled.salend.RectButton> = arrayOf(        //카테고리 버튼 배열
            binding.categoryBtn0,
            binding.categoryBtn1,
            binding.categoryBtn2,
            binding.categoryBtn3,
            binding.categoryBtn4,
            binding.categoryBtn5,
            binding.categoryBtn6,
            binding.categoryBtn7
        )
        binding.locationTv.text = MyApplication.sharedPref.getString("location","지역")
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {      //액티비티 결과 콜백 지정 
                if (it.resultCode == Activity.RESULT_OK) {
                    binding.locationTv.text = it.data?.getStringExtra("locationResult")
                }
            }
        binding.locationTv.setOnClickListener {     //지역 텍스트 뷰 클릭 시 LocationSelectActivity 실행
            val intent = Intent(this.context, LocationSelectActivity::class.java)
            intent.putExtra("location", binding.locationTv.text.toString())
            activityResultLauncher.launch(intent)


        }
        for(i: Int in btnList.indices){
            btnList[i].setOnClickListener {
                val intent01 = Intent(this.context, KategorieActivity::class.java)
                intent01.putExtra("storeName", btnList[i].text.toString())
                startActivity(intent01)
            }
        }

        return initRecyclerView()
    }

    companion object {
        /**`
         * Use this factory method to create a new instance of
         * this cf.untitled.salend.fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of cf.untitled.salend.fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initRecyclerView(): View? {     //retrofit통신으로, ProductArray형태를 받아 옴
        var nearbyProductList = ArrayList<ProductData>()
        var endTimeProductList = ArrayList<ProductData>()
        RetrofitClass.service.getProductArrayPage("test2.html").enqueue(object : Callback<ProductArray> {
            override fun onResponse(call: Call<ProductArray>, response: Response<ProductArray>) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    var result: ProductArray? = response.body()
                    result?.near_by?.let { nearbyProductList.addAll(it) }
                    result?.end_time?.let { endTimeProductList.addAll(it) }
                    recyclerSetData(nearbyProductList, endTimeProductList)

                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("retrofit", "${response.code()}")
                    Log.d("retrofit", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<ProductArray>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("retrofit", "onFailure 에러: " + t.message.toString());
            }
        })
        return binding.root
    }

    private fun recyclerSetData(        //리사이클러 뷰 데이터 지정
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