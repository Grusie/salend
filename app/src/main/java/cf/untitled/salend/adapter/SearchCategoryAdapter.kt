package cf.untitled.salend.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.MyApplication
import cf.untitled.salend.SearchActivity
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.model.CategoryData
import kotlin.concurrent.thread

class SearchCategoryAdapter(private var data: ArrayList<CategoryData>) :
    RecyclerView.Adapter<SearchCategoryAdapter.SearchCategoryViewHolder>() {
    class SearchCategoryViewHolder(binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var category = binding.searchCategory
    }
    override fun onBindViewHolder(holder: SearchCategoryViewHolder, position: Int) {
        holder.category.text = data[position].category
        holder.category.setOnClickListener {
            SearchActivity.searchCategory(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCategoryViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
