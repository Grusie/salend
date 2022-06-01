package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.adapter.NearbySaleRecyclerAdapter
import cf.untitled.salend.adapter.SearchCategoryAdapter
import cf.untitled.salend.adapter.SearchListAdaper
import cf.untitled.salend.databinding.ActivitySearchBinding
import cf.untitled.salend.model.CategoryData
import cf.untitled.salend.model.SearchData

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        var searchListArray = ArrayList<SearchData>()
        searchListArray.add(SearchData("asd"))
        searchListArray.add(SearchData("qwe"))
        searchListArray.add(SearchData("zxc"))
        binding.searchSearchListRecycler.apply{
            adapter = SearchListAdaper(searchListArray)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
}