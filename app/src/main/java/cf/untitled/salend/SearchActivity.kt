package cf.untitled.salend

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    var flag = 0
    var productResult = ArrayList<ProductData>()
    var storeResult = ArrayList<StoreData>()

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
                        if(flag == 0) {
                            searchProduct(query)
                        }
                        else {
                            searchStore(query)
                        }
                        binding.categoryTextView.visibility = View.INVISIBLE
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        val stringArray = resources.getStringArray(R.array.category)

        for( i in stringArray) {
            categoryArray.add(CategoryData(i))
        }

        val categoryAdapter = SearchCategoryAdapter(categoryArray)

        categoryAdapter.itemClick = object: SearchCategoryAdapter.ItemClick{
            override fun onClick(view: View, position: Int, text : String){
                if(flag == 0 ){
                    var productCategory = ArrayList<ProductData>()
                    for(i in productResult) {
                        if(i.i_tag == position)
                            productCategory.add(i)
                    }
                    if(productResult.size != 0) {
                        binding.categoryTextView.text = text
                        binding.categoryTextView.visibility = View.VISIBLE
                    }
                    initSearchProductRecycler(productCategory)
                }else {
                    var storeCategory = ArrayList<StoreData>()
                    for(i in storeResult) {
                        if(i.s_tag?.contains(position) == true)
                            storeCategory.add(i)
                    }
                    if(storeResult.size != 0) {
                        binding.categoryTextView.text = text
                        binding.categoryTextView.visibility = View.VISIBLE
                    }
                    initSearchStoreRecycler(storeCategory)
                }

            }
        }
        binding.searchCategoryRecycler.apply{
            adapter = categoryAdapter
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
            layoutManager = LinearLayoutManager(this@SearchActivity)
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
                        productResult = result.items
                        initSearchProductRecycler(productResult)
                    } else {
                        initSearchProductRecycler(ArrayList())
                        binding.searchExpText.text = "\""+query+"\""+"(은)는 존재하지 않습니다."
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
                        storeResult = result.stores
                        initSearchStoreRecycler(storeResult)
                    } else {
                        binding.searchExpText.text = "\""+query+"\""+"(은)는 존재하지 않습니다."
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