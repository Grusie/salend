package cf.untitled.salend.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.MyApplication
import cf.untitled.salend.adapter.PayAdapter
import cf.untitled.salend.databinding.FragmentPayListBinding
import cf.untitled.salend.model.BuyData
import cf.untitled.salend.model.BuyList
import cf.untitled.salend.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PayListFragment : Fragment() {        //파이어베이스에서 결제한 상품의 id 값을 받아와 정보를 띄워주는 결제 내역 프래그먼트
    lateinit var binding: FragmentPayListBinding

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        var payList: ArrayList<String?>
        val buyList = ArrayList<BuyData>()

        fun setRecyclerData(buyList: ArrayList<BuyData>) {
            binding.payListRv.adapter = PayAdapter(buyList)
            binding.payListRv.layoutManager = LinearLayoutManager(context)
        }

        if (isVisibleToUser) {
            if (MyApplication.checkLogIn()) {
                Thread {
                    run {
                        payList = MyApplication.getPayList(MyApplication.current_user_email!!)
                        val payRetrofit = payList.joinToString(" ")
                        Log.d("retrofit", "$payRetrofit")
                        RetrofitClass.service.getBuyList(payRetrofit).enqueue(object :
                            Callback<BuyList> {
                            override fun onResponse(
                                call: Call<BuyList>,
                                response: Response<BuyList>
                            ) {
                                if (response.isSuccessful) {
                                    // 정상적으로 통신이 성공된 경우
                                    val result: BuyList? = response.body()
                                    Log.d("retrofit", "payResult = ${result}")
                                    result?.let { buyList.addAll(it.buys) }
                                    setRecyclerData(buyList)

                                } else {
                                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                                    Log.d("retrofit", "${response.code()}")
                                    Log.d("retrofit", "onResponse 실패")
                                }
                            }

                            override fun onFailure(call: Call<BuyList>, t: Throwable) {
                                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                                Log.d("retrofit", "onFailure 에러: " + t.message.toString())
                            }
                        })
                    }
                }.start()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayListBinding.inflate(layoutInflater)

        return binding.root
    }
}