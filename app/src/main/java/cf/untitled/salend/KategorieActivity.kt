package cf.untitled.salend

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.adapter.KategorieChoiceAdapter
import cf.untitled.salend.databinding.KategorieChoiceBinding
import cf.untitled.salend.interface2.IStoreService
import cf.untitled.salend.model.KategoriStore
import cf.untitled.salend.model.KategoriStoreData
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class KategorieActivity: AppCompatActivity() {

    lateinit var binding: KategorieChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KategorieChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeName = intent.getStringExtra("storeName")
        binding.kategorieChoiceToolbarText.setText(storeName)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://salend.tk/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IStoreService::class.java)

        service.stores().enqueue(object : Callback<KategoriStore>{
            override fun onResponse(call: Call<KategoriStore>, response: Response<KategoriStore>) {
                val customAdapter = KategorieChoiceAdapter()
                binding.kategorieRv.adapter = customAdapter
                val result = response.body()
                customAdapter.storeList = result
                Toast.makeText(this@KategorieActivity,"${customAdapter.storeList?.size}",Toast.LENGTH_LONG).show()
                binding.kategorieRv.layoutManager = GridLayoutManager(this@KategorieActivity,2,GridLayoutManager.VERTICAL,false)
            }

            override fun onFailure(call: Call<KategoriStore>, t: Throwable) {
                Log.e("카테고리액티비티","${t.localizedMessage}")
            }
        })
    }
}