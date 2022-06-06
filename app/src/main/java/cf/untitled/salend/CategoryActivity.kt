package cf.untitled.salend

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.adapter.CategoryChoiceAdapter
import cf.untitled.salend.databinding.CategoryChoiceBinding
import cf.untitled.salend.interface2.IStoreService
import cf.untitled.salend.model.CategoryStore
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: CategoryChoiceBinding
    lateinit var save : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CategoryChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeName = intent.getStringExtra("storeName")
        binding.categoryChoiceToolbarText.setText(storeName)

        save = storeName.toString()



        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IStoreService::class.java)

        service.stores().enqueue(object : Callback<CategoryStore> {
            override fun onResponse(call: Call<CategoryStore>, response: Response<CategoryStore>) {

                val customAdapter = CategoryChoiceAdapter()
                binding.categoryRv.adapter = customAdapter
                val result = response.body()
                customAdapter.storeList = result
                customAdapter.save = save
                binding.categoryRv.layoutManager = GridLayoutManager(this@CategoryActivity, 2, GridLayoutManager.VERTICAL, false)
            }

            override fun onFailure(call: Call<CategoryStore>, t: Throwable) {
                Log.e("카테고리액티비티", "${t.localizedMessage}")
            }
        })
    }
}