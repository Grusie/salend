package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.adapter.SearchCategoryAdapter
import cf.untitled.salend.databinding.ActivitySearchBinding
import cf.untitled.salend.model.CategoryData
import cf.untitled.salend.model.ProductData

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.searchToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchSearchView.apply{
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        var categoryArray = ArrayList<CategoryData>()
        categoryArray.add(CategoryData("한식"))
        categoryArray.add(CategoryData("양식"))
        categoryArray.add(CategoryData("중식"))
        Log.d("category","$categoryArray")
        binding.searchCategoryRecycler.apply{
            adapter = SearchCategoryAdapter(categoryArray)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        var searchListArray = ArrayList<ProductData>()
        searchListArray.add(ProductData("asdsa","asdzxc","asd","ASd","Asdas",0,0,0,""))
        searchListArray.add(ProductData("asdsa","asdzxc","asd","ASd","Asdas",0,0,0,""))
        searchListArray.add(ProductData("asdsa","asdzxc","asd","ASd","Asdas",0,0,0,""))
        if(searchListArray.size > 0)
            binding.searchExpText.visibility = View.GONE
        else
            binding.searchExpText.visibility = View.VISIBLE

        binding.searchSearchListRecycler.apply{
            adapter = NearbySaleRecyclerAdapter(searchListArray)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
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
}