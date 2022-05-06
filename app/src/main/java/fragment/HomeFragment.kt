package fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.LocationSelectActivity
import cf.untitled.salend.R
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.databinding.FragmentHomeBinding
import cf.untitled.salend.model.ProductData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        binding = FragmentHomeBinding.inflate(layoutInflater)

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                binding.locationTv.text = it.data?.getStringExtra("locationResult");
            }
        }
        binding.locationTv.setOnClickListener{
            val intent = Intent(this.context, LocationSelectActivity::class.java)
            intent.putExtra("location",binding.locationTv.text.toString())
            activityResultLauncher.launch(intent)
        }
        return initRecyclerView()
    }

    companion object {
        /**`
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
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
    private fun initRecyclerView() : View? {
        val productList = arrayOf(
                ProductData(0,"아메리카노","GS25 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",3000,2000,null,null,null),
                ProductData(0,"캔디","CU 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",4000,2000,null,null,null),
                ProductData(0,"디사론노","hello 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",3000,2000,null,null,null),
                ProductData(0,"장갑","A 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",5000,2000,null,null,null),
                ProductData(0,"마우스","B 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",6000,3000,null,null,null),
                ProductData(0,"빵","C 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",5000,3000,null,null,null),
                ProductData(0,"회","D 면목 4동점","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt559x4ig-wTTMPcj3W9LD0dLvY6Ggi1E4L0WAP3IWcQ&s",6000,1000,null,null,null),
            )
        binding.nearbySaleRecyclerview.apply{
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = NearbySaleRecyclerAdapter(productList)
        }

        binding.deadlineSaleRecyclerview.apply{
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = NearbySaleRecyclerAdapter(productList)
        }
        return binding.root
    }
}