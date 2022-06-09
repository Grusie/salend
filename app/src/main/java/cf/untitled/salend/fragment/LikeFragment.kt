package cf.untitled.salend.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
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
private lateinit var favArray: ArrayList<String?>
private lateinit var favItemArray : ArrayList<String?>
private var initFlag = false
private var itemInit = false

class LikeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var f: FragmentLikeBinding
    val storeAdapter = FavoriteStoreAdapter()
    val itemAdpater = FavoriteProductAdpater()
    lateinit var favorStoreList2: ArrayList<StoreData>
    lateinit var favorItemList : ArrayList<ProductData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        initFlag = true;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this cf.untitled.salend.fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        f = FragmentLikeBinding.bind(view)
        val TAG = "LF";

        val thread = Thread {
            run {
                Log.e(TAG, "onViewCreated: " + MyApplication.setStatus())
                if (MyApplication.setStatus()) {
                    favArray = MyApplication.getStoreFavorite()
                    favItemArray = MyApplication.getProductFavorite()
                    getRequest()
                    Log.e("like", favArray.toString())
                }
            }
        }

            f.favoritePageStoreButton.setOnClickListener {
                getRequest()
                storeAdapter.favorStoreList = favorStoreList2
                f.favoritePageItemRecyclerview.adapter = storeAdapter
                f.favoritePageItemRecyclerview.layoutManager = GridLayoutManager(context, 2)
            }

        f.favoritePageItemButton.setOnClickListener {
            if(itemInit) {
                getRequest2()
                itemAdpater.productArray = favorItemList
                f.favoritePageItemRecyclerview.adapter = itemAdpater
                f.favoritePageItemRecyclerview.layoutManager = GridLayoutManager(context, 2)
            }
        }

        favorStoreList2 = ArrayList()
        storeAdapter.favorStoreList = favorStoreList2
        f.favoritePageItemRecyclerview.adapter = storeAdapter
        f.favoritePageItemRecyclerview.layoutManager = GridLayoutManager(context, 2)


        if (thread.state == Thread.State.NEW)
            thread.start()

    }

    fun refreshList() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.me { user, error ->
                if(error != null){
                    Log.e("grusie", "사용자 정보 요청 실패", error)
                }
                else if(user != null){
                    MyApplication.current_user_email = user.id.toString()
                    favArray = MyApplication.getStoreFavorite()
                    getRequest()    // 파이어베이스에서 꺼낸 값을 서버로 요청을 보냄
                }
            }
            return
        }
        if (MyApplication.setStatus()) {
            favArray = MyApplication.getStoreFavorite()
            favItemArray = MyApplication.getProductFavorite()
            getRequest()   // 파이어베이스에서 꺼낸 값을 서버로 요청을 보냄
            getRequest2()
            val TAG = "LF"
            Log.e(TAG, "refreshList: " + favArray.toString())
        }
    }

    fun getRequest2() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        val itemRetrofit = favItemArray.toString().replace("[", "").replace("]", "").replace(",", "")
        if(itemRetrofit.trim() == ""){
            favorItemList = ArrayList()
            return
        }
        service.getItemFavor(itemRetrofit).enqueue(object : Callback<ProductArray2> {
            override fun onResponse(call: Call<ProductArray2>, response: Response<ProductArray2>) {
                favorItemList = response.body()!!.items
                itemInit = true
            }

            override fun onFailure(call: Call<ProductArray2>, t: Throwable) {
                t.printStackTrace()
                Log.d("like2", "서버통신실패")
            }
        })

    }

    fun getRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        val str = favArray.toString().replace("[", "").replace("]", "").replace(",", "")

        if (str.trim() == "") {
            favorStoreList2 = ArrayList()
            return
        }
        service.getStoreFavor(str).enqueue(object : Callback<StoreArray> {
            override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>) {
                // 서버에 요청을 해서 성공한 경우
                favorStoreList2 = response.body()!!.stores
                Log.e("LF", "onResponse: " + response.body()!!.stores[0].s_name)
            }

            override fun onFailure(call: Call<StoreArray>, t: Throwable) {
                t.printStackTrace()
                Log.d("like", "서버통신실패")
            }
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (initFlag) {
            Thread {
                run {
                    refreshList()
                }
            }.start()
        }
    }
}
