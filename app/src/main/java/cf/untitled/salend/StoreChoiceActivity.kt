package cf.untitled.salend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.adapter.StoreChoiceAdapter
import cf.untitled.salend.databinding.ActivityStoreChoiceBinding
import cf.untitled.salend.interface2.IStoreService
import cf.untitled.salend.model.KategoriStore
import cf.untitled.salend.model.ProductArray
import cf.untitled.salend.retrofit.RetrofitClass
import cf.untitled.salend.retrofit.RetrofitService
import com.bumptech.glide.Glide
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

class StoreChoiceActivity : AppCompatActivity(), View.OnClickListener {
    var star = false
    val binding by lazy { ActivityStoreChoiceBinding.inflate(layoutInflater) }
    lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.kategorieChoiceToolbarText.text = intent.getStringExtra("kategori")
        binding.activityStoreStoreName.text = intent.getStringExtra("name")
        binding.activityStoreTime.text = intent.getStringExtra("time")

        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .error("https://media.vlpt.us/images/sasy0113/post/f7085683-1a62-4ce7-9f7f-e8fd2f3ec825/Android%20Kotlin.jpg")
            .into(binding.activityStoreImage)



        RetrofitClass.service.getTest2page().enqueue(object : Callback<ProductArray> {
            override fun onResponse(call: Call<ProductArray>, response: Response<ProductArray>) {
                val customAdapter = StoreChoiceAdapter()
                binding.activityStoreRecyclerview.adapter = customAdapter
                customAdapter.itemList = response.body()!!
                binding.activityStoreRecyclerview.layoutManager = GridLayoutManager(this@StoreChoiceActivity,2)
            }

            override fun onFailure(call: Call<ProductArray>, t: Throwable) {
                Log.i("StoreChoiceActivity:서버통신", t.localizedMessage)
            }
        }
        )

        binding.kategorieChoiceFavoriteButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if(v == binding.kategorieChoiceFavoriteButton) {
            if(star) {
                binding.kategorieChoiceFavoriteButton.setBackgroundResource(R.drawable.favorite_off)
                star = false
            } else {
                binding.kategorieChoiceFavoriteButton.setBackgroundResource(R.drawable.favorite_on)
                star = true
            }
        }
    }
}
