package cf.untitled.salend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.MyApplication.Companion.db
import cf.untitled.salend.adapter.StoreChoiceAdapter
import cf.untitled.salend.databinding.ActivityStoreChoiceBinding
import cf.untitled.salend.interface2.IStoreService
import cf.untitled.salend.model.FireUserData
import cf.untitled.salend.model.CategoryStore
import cf.untitled.salend.model.ProductArray
import cf.untitled.salend.model.StoreItamData
import cf.untitled.salend.retrofit.RetrofitClass
import cf.untitled.salend.retrofit.RetrofitService
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileNotFoundException
import java.net.URL
import kotlin.concurrent.thread

class StoreChoiceActivity : AppCompatActivity(){
    var star = false
    val binding by lazy { ActivityStoreChoiceBinding.inflate(layoutInflater) }
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        val store = Firebase.firestore


        setSupportActionBar(binding.storeChoiceToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.categoryChoiceToolbarText.text = intent.getStringExtra("kategori")
        binding.activityStoreStoreName.text = intent.getStringExtra("name")
        binding.activityStoreTime.text = intent.getStringExtra("time")
        val storeId = intent.getStringExtra("id")
        Log.d("sang", storeId.toString())

        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .error("https://media.vlpt.us/images/sasy0113/post/f7085683-1a62-4ce7-9f7f-e8fd2f3ec825/Android%20Kotlin.jpg")
            .into(binding.activityStoreImage)



        RetrofitClass.service.getTest2page().enqueue(object : Callback<StoreItamData> {
            override fun onResponse(call: Call<StoreItamData>, response: Response<StoreItamData>) {
                Log.i("sibar", response.body().toString())
                val customAdapter = StoreChoiceAdapter()
                binding.activityStoreRecyclerview.adapter = customAdapter
                customAdapter.itemList = response.body()!!
                binding.activityStoreRecyclerview.layoutManager = GridLayoutManager(this@StoreChoiceActivity,2)
            }

            override fun onFailure(call: Call<StoreItamData>, t: Throwable) {
                Log.i("StoreChoiceActivity:서버통신", t.localizedMessage)
            }
        })


        if (MyApplication.current_user_email == null) {
            binding.favoriteImageBtn.visibility = View.INVISIBLE
        } else {
            binding.favoriteImageBtn.visibility = View.VISIBLE
        }

        thread(start = true) {
            val storeFavorList = MyApplication.getStoreFavorite()

            for (i in 0..storeFavorList.size-1) {
                if(storeId == storeFavorList[i]) {
                    binding.favoriteImageBtn.setImageResource(R.drawable.favorite_on)
                    star = true
                }
            }
        }


        binding.favoriteImageBtn.setOnClickListener {

            if(star) {  //눌러져있으면
                binding.favoriteImageBtn.setImageResource(R.drawable.ic_favorite_svgrepo_com)
                thread(start = true) {
                    MyApplication.delStoreFavorite(MyApplication.current_user_email!!, storeId!!)
                }
                star = false
            } else {   //안눌러져있을때
                binding.favoriteImageBtn.setImageResource(R.drawable.ic_favorite_selected)
                star = true
                thread(start=true) {
                    MyApplication.setStoreFavorite(MyApplication.current_user_email!!, storeId!!)
                }
            }
        }
    }


}
