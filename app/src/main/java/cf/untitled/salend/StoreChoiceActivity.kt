package cf.untitled.salend

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.databinding.ActivityStoreChoiceBinding
import cf.untitled.salend.model.ProductArray2
import cf.untitled.salend.model.StoreArray
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
    val binding by lazy { ActivityStoreChoiceBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    lateinit var storeId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        setSupportActionBar(binding.storeChoiceToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.activityStoreImgCardView.layoutParams.height =
            this.windowManager.defaultDisplay.width * 2 / 3
        storeId = intent.getStringExtra("id").toString()

        RetrofitClass.service.getStores().enqueue(object : Callback<StoreArray> {
            override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>) {
                if (response.body()?.stores != null) {
                    for (i in 0 until response.body()?.stores?.size!!) {
                        if (storeId == response.body()?.stores?.get(i)?._id) {
                            binding.activityStoreStoreName.text =
                                response.body()?.stores?.get((i))?.s_name ?: "noName"
                            binding.activityStoreTime.text =
                                response.body()?.stores?.get(i)?.s_time ?: "noTime"
                            binding.activityStoreArea.text =
                                response.body()?.stores?.get(i)?.s_address ?: "noAddress"
                            Glide.with(baseContext)
                                .load(response.body()?.stores?.get(i)?.s_image)
                                .error(R.drawable.ic_no_image_svgrepo_com)
                                .into(binding.activityStoreImage)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<StoreArray>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


        RetrofitClass.service.getStoreItem(storeId!!).enqueue(object : Callback<ProductArray2> {
            override fun onResponse(call: Call<ProductArray2>, response: Response<ProductArray2>) {
                if (response.body() != null) {
                    binding.activityStoreRecyclerview.adapter =
                        NearbySaleRecyclerAdapter(response.body()!!.items)
                }
                binding.activityStoreRecyclerview.layoutManager =
                    LinearLayoutManager(this@StoreChoiceActivity)
            }

            override fun onFailure(call: Call<ProductArray2>, t: Throwable) {
                Log.i("StoreChoiceActivity:서버통신", t.localizedMessage)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        if (MyApplication.current_user_email != null && MyApplication.current_user_email != "") {
            thread(start = true) {
                val flag = MyApplication.checkStoreFavorite(storeId)
                runOnUiThread {
                    if (flag) {
                        menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
                        menu?.getItem(0)?.isChecked = true
                    } else {
                        menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(this, R.drawable.ic_star)
                        menu?.getItem(0)?.isChecked = false
                    }
                }
            }
            menu?.getItem(0)?.isVisible = true
        } else menu?.getItem(0)?.isVisible = false


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite_menu) {
            if (item.isChecked) {
                changeFavorite(item, false)
            } else {
                changeFavorite(item, true)
            }
        }
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeFavorite(item: MenuItem, flag: Boolean) {
        if (flag) {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
            thread(start = true) {
                MyApplication.setStoreFavorite(MyApplication.current_user_email!!, storeId)
            }
            item.isChecked = true
        } else {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
            thread(start = true) {
                MyApplication.delProductFavorite(MyApplication.current_user_email!!, storeId)
            }
            item.isChecked = false
        }
    }
}
