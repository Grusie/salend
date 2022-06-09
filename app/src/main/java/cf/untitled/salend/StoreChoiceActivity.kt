package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.adapter.StoreChoiceAdapter
import cf.untitled.salend.databinding.ActivityStoreChoiceBinding
import cf.untitled.salend.model.StoreItemData
import cf.untitled.salend.retrofit.RetrofitClass
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class StoreChoiceActivity : AppCompatActivity() {
    var star = false
    val binding by lazy { ActivityStoreChoiceBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        setSupportActionBar(binding.storeChoiceToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.categoryChoiceToolbarText.text = intent.getStringExtra("category")
        binding.activityStoreStoreName.text = intent.getStringExtra("name")
        binding.activityStoreTime.text = intent.getStringExtra("time")

        val storeId = intent.getStringExtra("id")
        Log.e("SCA", "onCreate: ${storeId}" )

        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .error("https://media.vlpt.us/images/sasy0113/post/f7085683-1a62-4ce7-9f7f-e8fd2f3ec825/Android%20Kotlin.jpg")
            .into(binding.activityStoreImage)   // 에러시 error 이미지

        RetrofitClass.service.getStoreItem(storeId!!).enqueue(object : Callback<StoreItemData> {
            override fun onResponse(call: Call<StoreItemData>, response: Response<StoreItemData>) {
                val customAdapter = StoreChoiceAdapter()
                binding.activityStoreRecyclerview.adapter = customAdapter
                customAdapter.itemList = response.body()!!
                binding.activityStoreRecyclerview.layoutManager =
                    GridLayoutManager(this@StoreChoiceActivity, 2)
            }

            override fun onFailure(call: Call<StoreItemData>, t: Throwable) {
                Log.i("StoreChoiceActivity:서버통신", t.localizedMessage)
            }
        })

        if (MyApplication.current_user_email == null) {
            binding.favoriteImageBtn.visibility = View.INVISIBLE
        } else {
            binding.favoriteImageBtn.visibility = View.VISIBLE
        }

        Thread() {
            run {
                val check = MyApplication.checkStoreFavorite(storeId!!)
                if (check) {
                    runOnUiThread {
                        binding.favoriteImageBtn.setImageResource(R.drawable.favorite_on)
                        star = true
                    }
                }
            }
        }.start()

        binding.favoriteImageBtn.setOnClickListener {
            if (star) {  //눌러져있으면
                binding.favoriteImageBtn.setImageResource(R.drawable.ic_favorite_svgrepo_com)
                thread(start = true) {
                    MyApplication.delStoreFavorite(MyApplication.current_user_email!!, storeId!!)
                }
                star = false
            } else {   //안눌러져있을때
                binding.favoriteImageBtn.setImageResource(R.drawable.ic_favorite_selected)
                star = true
                thread(start = true) {
                    MyApplication.setStoreFavorite(MyApplication.current_user_email!!, storeId!!)
                }
            }
        }
    }


}
