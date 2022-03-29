package fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.R
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.databinding.FragmentHomeBinding
import cf.untitled.salend.model.productInfo

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
                productInfo(R.drawable.ic_search,"GS25 면목 4동점","아메리카노",2000),
                productInfo(R.drawable.ic_search,"CU 면목 4동점","캔디",500),
                productInfo(R.drawable.ic_search,"hello 면목 4동점","디사론노",12000),
                productInfo(R.drawable.ic_search,"A 면목 4동점","장갑",7000),
                productInfo(R.drawable.ic_search,"B 면목 4동점","마우스",7000),
                productInfo(R.drawable.ic_search,"C 면목 4동점","빵",3000),
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