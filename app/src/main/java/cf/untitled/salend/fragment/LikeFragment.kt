package cf.untitled.salend.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.adapter.FavoriteStoreAdapter
import cf.untitled.salend.databinding.FragmentLikeBinding
import cf.untitled.salend.model.StoreArray
import cf.untitled.salend.model.StoreData
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
private var initFlag = false

class LikeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var f : FragmentLikeBinding
    val adapter = FavoriteStoreAdapter()
    lateinit var favorStoreList2 : ArrayList<StoreData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        initFlag = true;
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    getRequest()
                    Log.e("like", favArray.toString())
                }
            }
        }

        f.favoritePageStoreButton.setOnClickListener {
            Log.d("grusie","${ MyApplication.current_user_email}")
/*            adapter.favorStoreList = favorStoreList2
            f.favoritePageItemRecyclerview.adapter = adapter
            f.favoritePageItemRecyclerview.layoutManager = GridLayoutManager(context, 2)*/
        }

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
            getRequest()    // 파이어베이스에서 꺼낸 값을 서버로 요청을 보냄
            val TAG = "LF"
            Log.e(TAG, "refreshList: " + favArray.toString())
        }
    }

    fun getRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)
        val str = favArray.toString().replace("[", "").replace("]", "").replace(",", "")

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
