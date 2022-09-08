package cf.untitled.salend.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.MyApplication
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.adapter.SearchStoreAdapter
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


class LikeFragment : Fragment() {       //찜 페이지 프래그먼트
    private lateinit var f: FragmentLikeBinding

    private lateinit var favStoreStrArray: ArrayList<String?>

    private lateinit var favItemStrArray: ArrayList<String?>
    var favorStoreList = ArrayList<StoreData>()
    var favorItemList = ArrayList<ProductData>()
    private lateinit var binding: FragmentLikeBinding

    var storeAdapter = SearchStoreAdapter(favorStoreList)
    var itemAdapter = NearbySaleRecyclerAdapter(favorItemList)

    private var TAG = "LF"
    private var mode = "Store"

    private var initFlag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initFlag = true
        binding = FragmentLikeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        f = FragmentLikeBinding.bind(view)

        val thread = Thread {
            run {
                favStoreStrArray = ArrayList()
                favItemStrArray = ArrayList()
                favorStoreList = ArrayList()
                favorItemList = ArrayList()

                refreshList()
            }
        }

        f.favoritePageStoreButton.setOnClickListener {
            f.favoritePageItemButton.isSelected = false
            f.favoritePageStoreButton.isSelected = true
            getRequest()
        }

        f.favoritePageItemButton.setOnClickListener {
            f.favoritePageItemButton.isSelected = true
            f.favoritePageStoreButton.isSelected = false
            getRequest2()
        }

        refreshItem()
        setMode("Item")
        f.favoritePageItemRecyclerview.layoutManager = LinearLayoutManager(this.context)

        if (thread.state == Thread.State.NEW)
            thread.start()

        initFlag = true
    }

    fun refreshStore(){
        storeAdapter = SearchStoreAdapter(favorStoreList)
    }
    fun refreshItem(){
        itemAdapter = NearbySaleRecyclerAdapter(favorItemList)
    }

    fun refreshList() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("grusie", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    MyApplication.current_user_email = user.id.toString()

                    Thread {
                        run {
                            favStoreStrArray = MyApplication.getStoreFavorite()
                            favItemStrArray = MyApplication.getProductFavorite()
                            getRequest2()    // 파이어베이스에서 꺼낸 값을 서버로 요청을 보냄
                        }
                    }.start()
                }
            }
            return
        }
        if (MyApplication.setStatus()) {
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
            refreshStore()
            return
        }

        service.getStoreFavor(str).enqueue(object : Callback<StoreArray> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>) {
                // 서버에 요청을 해서 성공한 경우
                favorStoreList = response.body()!!.stores
                refreshStore()
                binding.favoriteTextList.text = "${favorStoreList.size}개의 찜 목록"
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
            refreshItem()
            return
        }
        service.getItemFavor(itemRetrofit).enqueue(object : Callback<ProductArray2> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ProductArray2>, response: Response<ProductArray2>) {
                favorItemList = response.body()!!.items
                refreshItem()
                binding.favoriteTextList.text = "${favorItemList.size}개의 찜 목록"
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

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (initFlag) {
                Thread {
                    run {
                        f.favoritePageItemButton.isSelected = true
                        f.favoritePageStoreButton.isSelected = false
                        refreshList()
                    }
                }.start()
            }
        }
    }
}