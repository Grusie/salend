package cf.untitled.salend

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import cf.untitled.salend.databinding.ActivityProductBinding
import cf.untitled.salend.model.ProductData
import cf.untitled.salend.retrofit.RetrofitClass
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class ProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        initProduct("6288e7d2e747d7702b9c4986")
        setContentView(binding.root)

        //val productId = intent.getStringExtra("product_id")
        setSupportActionBar(binding.productToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.testFire.setOnClickListener {
            thread(start=true) {
                MyApplication.setStoreFavorite(
                    MyApplication.current_user_email,
                    "628edf00a1639ae216017c70"
                )
            }
        }

    }

    private fun initProduct(productId : String?){
        RetrofitClass.service.getSingleProductDataPage("$productId").enqueue(object :
            Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                if (response.isSuccessful) {
                    var saleRate = 0
                    // 정상적으로 통신이 성공된 경우
                    var result: ProductData? = response.body()
                    saleRate = 100 - ((result?.i_now_price!!.toDouble() / result?.i_price!!.toDouble()) * 100).toInt()
                    Log.d("productInfo", "${result?.i_now_price}" )
                    Log.d("productInfo", "${result?.i_price}")
                    Log.d("productInfo", "${saleRate}")
                    binding.apply {
                        Glide.with(this@ProductActivity)
                            .load(result?.i_image)
                            .error(R.drawable.ic_map_svgrepo_com)
                            .into(productInfoImg)
                        productInfoName.text = result?.i_name

                        productInfoPrice.text = result?.i_price.toString() + " -> " + result?.i_now_price.toString() +"("+ saleRate +"%할인!)"
                        productInfoExp.text = result?.i_exp
                    }
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("retrofit", "${response.code()}")
                    Log.d("retrofit", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("retrofit", "onFailure 에러: " + t.message.toString());
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        thread(start = true){
            val flag = MyApplication.checkStoreFavorite("628edf00a1639ae216017c70")
            runOnUiThread {
                if(flag)
                    menu?.getItem(0)?.icon = ContextCompat.getDrawable(this@ProductActivity,R.drawable.ic_favorite_selected)
                else
                    menu?.getItem(0)?.icon = ContextCompat.getDrawable(this@ProductActivity,R.drawable.ic_favorite_svgrepo_com)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }
}