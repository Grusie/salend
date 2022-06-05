package cf.untitled.salend

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.adapter.SearchCategoryAdapter
import cf.untitled.salend.adapter.SearchStoreAdapter
import cf.untitled.salend.databinding.ActivitySearchBinding
import cf.untitled.salend.model.*
import cf.untitled.salend.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    var flag = 0
    companion object{
        fun searchCategory(position : Int){
                var storeCategoryResult: StoreArray? = null
                RetrofitClass.service.getStoreCategoryPage(position).enqueue(object:
                    Callback<StoreArray> {
                    override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>){
                        if(response.isSuccessful) {
                            var result: StoreArray? = response.body()
                            storeCategoryResult = result
                            Log.d("retrofit","$result")
                        }
                        else {
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                            Log.d("retrofit", "${response.code()}")
                            Log.d("retrofit", "onResponse 실패")
                        }
                    }
                    override fun onFailure(call: Call<StoreArray>, t: Throwable) {
                        TODO("Not yet implemented")}
                })
        }
    }
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.searchToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var categoryArray = ArrayList<CategoryData>()

        binding.productTabBtn.setOnClickListener{
            if(flag == 1) {
                flag = 0
                binding.productTabBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                binding.storeTabBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.salend_background))
                binding.searchSearchView.setQuery(binding.searchSearchView.query, true)
            }
        }
        binding.storeTabBtn.setOnClickListener {
            if(flag == 0){
                flag = 1
                binding.storeTabBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                binding.productTabBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.salend_background))
                binding.searchSearchView.setQuery(binding.searchSearchView.query, true)
            }
        }
        binding.searchSearchView.apply{
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) {
                        if(flag == 0)
                            searchProduct(query)
                        else
                            searchStore(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        categoryArray.add(CategoryData("한식"))
        categoryArray.add(CategoryData("양식"))
        categoryArray.add(CategoryData("중식"))
        Log.d("category","$categoryArray")
        binding.searchCategoryRecycler.apply{
            adapter = SearchCategoryAdapter(categoryArray)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun initSearchProductRecycler(productArray: ArrayList<ProductData>){
        binding.searchSearchListRecycler.apply{
            adapter = NearbySaleRecyclerAdapter(productArray)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
    fun initSearchStoreRecycler(storeArray: ArrayList<StoreData>) {
        binding.searchSearchListRecycler.apply {
            adapter = SearchStoreAdapter(storeArray)
            layoutManager = GridLayoutManager(this@SearchActivity,2)
            setHasFixedSize(true)
        }
    }

    fun searchProduct(query : String){
        RetrofitClass.service.getProductSearchPage(query).enqueue(object:
            Callback<ProductArray2>{
            override fun onResponse(
                call: Call<ProductArray2>,
                response: Response<ProductArray2>
            ) {
                if(response.isSuccessful) {
                    var result: ProductArray2? = response.body()
                    if (result != null && result.items.size != 0) {
                        binding.searchExpText.visibility = View.GONE
                        Log.d("retrofit","$result")
                        initSearchProductRecycler(result.items)
                    } else {
                        initSearchProductRecycler(ArrayList())
                        binding.searchExpText.visibility = View.VISIBLE
                    }
                }
                else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("retrofit", "${response.code()}")
                    Log.d("retrofit", "onResponse 실패")
                }
            }
            override fun onFailure(call: Call<ProductArray2>, t: Throwable) {
                TODO("Not yet implemented")}
        })
    }

    fun searchStore(query: String){
        RetrofitClass.service.getStoreSearchPage(query).enqueue(object: Callback<StoreArray>{
            override fun onResponse(call: Call<StoreArray>, response: Response<StoreArray>) {
                if(response.isSuccessful) {
                    var result: StoreArray? = response.body()
                    if (result != null && result.stores.size != 0) {
                        binding.searchExpText.visibility = View.GONE
                        Log.d("retrofit", "$result")
                        initSearchStoreRecycler(result.stores)
                    } else {
                        binding.searchExpText.visibility = View.VISIBLE
                        initSearchStoreRecycler(ArrayList())
                    }
                }
            }

            override fun onFailure(call: Call<StoreArray>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}