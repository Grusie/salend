package cf.untitled.salend.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.adapter.FavoriteProductAdpater
import cf.untitled.salend.adapter.FavoriteStoreAdapter
import cf.untitled.salend.databinding.FragmentLikeBinding
import cf.untitled.salend.model.*
import cf.untitled.salend.retrofit.RetrofitService
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LikeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var f: FragmentLikeBinding

    val storeAdapter = FavoriteStoreAdapter()
    val itemAdapter = FavoriteProductAdpater()

    private lateinit var favStoreStrArray: ArrayList<String?>
    private lateinit var favItemStrArray: ArrayList<String?>
    lateinit var favorStoreList: ArrayList<StoreData>
    lateinit var favorItemList: ArrayList<ProductData>

    private lateinit var binding: FragmentLikeBinding

    private var TAG = "LF";
    private var mode = "Store"

    private var initFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this cf.untitled.salend.fragment
        initFlag = true;
        binding = FragmentLikeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        f = FragmentLikeBinding.bind(view)
        val TAG = "LF";

        val thread = Thread {
            run {
                Log.e(TAG, "onViewCreated: RUN?" )
                favStoreStrArray = ArrayList()
                favItemStrArray = ArrayList()
                favorStoreList = ArrayList()
                favorItemList = ArrayList()
                refreshList()
            }
        }

        f.favoritePageStoreButton.setOnClickListener {
            getRequest()

        }

        f.favoritePageItemButton.setOnClickListener {
            getRequest2()

        }

        storeAdapter.favorStoreList = ArrayList()
        setMode("Store")
        f.favoritePageItemRecyclerview.layoutManager = GridLayoutManager(context, 2)

        if (thread.state == Thread.State.NEW)
            thread.start()

        initFlag = true
    }


    fun refreshList() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("grusie", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    MyApplication.current_user_email = user.id.toString()

                    Thread() {
                        run {
                            Log.e(TAG, "refreshList: KakaoRefresh" )
                            favStoreStrArray = MyApplication.getStoreFavorite()
                            favItemStrArray = MyApplication.getProductFavorite()
                            getRequest()    // 파이어베이스에서 꺼낸 값을 서버로 요청을 보냄
                        }
                    }.start()
                }
            }
            return
        }
        if (MyApplication.setStatus()) {
            Log.e(TAG, "refreshList: GRefresh" )
            favStoreStrArray = MyApplication.getStoreFavorite()
            favItemStrArray = MyApplication.getProductFavorite()
            getRequest()   // 파이어베이스에서 꺼낸 값을 서버로 요청을 보냄
        }
    }


    fun getRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        val str = favStoreStrArray.toString().replace("[", "").replace("]", "").replace(",", "")

        if (str.trim() == "") {
            favorStoreList = ArrayList()
            storeAdapter.favorStoreList =  favorStoreList
            return
        }

        service.getStoreFavor(str).enqueue(object : Callback<StoreArray> {
            override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>) {
                // 서버에 요청을 해서 성공한 경우
                favorStoreList = response.body()!!.stores
                Log.e(TAG, "onResponse: stores ${favorStoreList.size}" )
                storeAdapter.favorStoreList = favorStoreList
                binding.liketextlist.text = "${favorStoreList.size}개의 찜 목록"
                binding.favoritePageStoreButton.setTextColor(Color.parseColor("#6BD089"))
                binding.favoritePageItemButton.setTextColor(Color.parseColor("#808080"))

                setMode("Store")
            }

            override fun onFailure(call: Call<StoreArray>, t: Throwable) {
                t.printStackTrace()
                Log.d("like", "서버통신실패")
            }
        })
    }

    fun getRequest2() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        val itemRetrofit =
            favItemStrArray.toString().replace("[", "").replace("]", "").replace(",", "")
        if (itemRetrofit.trim() == "") {
            favorItemList = ArrayList()
            itemAdapter.productArray = favorItemList
            return
        }
        service.getItemFavor(itemRetrofit).enqueue(object : Callback<ProductArray2> {
            override fun onResponse(call: Call<ProductArray2>, response: Response<ProductArray2>) {
                favorItemList = response.body()!!.items
                Log.e(TAG, "onResponse: Item ${favorItemList.size}" )
                itemAdapter.productArray = favorItemList
                binding.liketextlist.text = "${favorItemList.size}개의 찜 목록"
                binding.favoritePageItemButton.setTextColor(Color.parseColor("#6BD089"))
                binding.favoritePageStoreButton.setTextColor(Color.parseColor("#808080"))
                setMode("Item")
            }

            override fun onFailure(call: Call<ProductArray2>, t: Throwable) {
                t.printStackTrace()
                Log.d("like2", "서버통신실패")
            }
        })
    }

    private fun setMode(txt: String) {
        this.mode = txt
        if(mode == "Store"){
            f.favoritePageItemRecyclerview.adapter = storeAdapter

        }
        else if(mode == "Item") {
            f.favoritePageItemRecyclerview.adapter = itemAdapter

        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.e(TAG, "setUserVisibleHint: initFlag ${initFlag}" )
        if (initFlag) {
            Thread {
                run {
                    refreshList()
                }
            }.start()
        }
    }
}