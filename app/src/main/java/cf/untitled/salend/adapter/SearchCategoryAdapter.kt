package cf.untitled.salend.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.SearchActivity
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.databinding.ItemSearchCategoryBinding
import cf.untitled.salend.model.CategoryData
import kotlin.concurrent.thread

class SearchCategoryAdapter(private var data: ArrayList<CategoryData>) :
    RecyclerView.Adapter<SearchCategoryAdapter.SearchCategoryViewHolder>() {
    interface ItemClick{
        fun onClick(view : View, position : Int, text: String)
    }
    var itemClick : ItemClick? = null
    class SearchCategoryViewHolder(binding: ItemSearchCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var category = binding.searchCategory
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: SearchCategoryViewHolder, position: Int) {
        holder.category.text = data[position].category
        if(itemClick != null) {
            holder.category.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it, position, holder.category.text as String)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCategoryAdapter.SearchCategoryViewHolder {
        val view = ItemSearchCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchCategoryAdapter.SearchCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
