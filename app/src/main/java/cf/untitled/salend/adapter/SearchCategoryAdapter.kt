package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemSearchCategoryBinding
import cf.untitled.salend.model.CategoryData

//검색페이지에서 카테고리 리사이클러뷰 구조 구현
class SearchCategoryAdapter(private var data: ArrayList<CategoryData>) :
    RecyclerView.Adapter<SearchCategoryAdapter.SearchCategoryViewHolder>() {
    interface ItemClick {
        fun onClick(view: View, position: Int, text: String)
    }

    var itemClick: ItemClick? = null

    class SearchCategoryViewHolder(binding: ItemSearchCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var category = binding.searchCategory
    }

    override fun onBindViewHolder(holder: SearchCategoryViewHolder, position: Int) {
        holder.category.text = data[position].category
        if (itemClick != null) {
            holder.category.setOnClickListener {
                itemClick?.onClick(it, position, holder.category.text as String)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchCategoryViewHolder {
        val view =
            ItemSearchCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
