package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.databinding.ItemSearchListBinding
import cf.untitled.salend.model.SearchData

class SearchListAdaper(private var data: ArrayList<SearchData>) :
    RecyclerView.Adapter<SearchListAdaper.SearchListViewHolder>() {
    class SearchListViewHolder(binding: ItemSearchListBinding) : RecyclerView.ViewHolder(binding.root){
        var list = binding.searchListTextView
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListAdaper.SearchListViewHolder {
        val view = ItemSearchListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchListAdaper.SearchListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListAdaper.SearchListViewHolder, position: Int) {
        holder.list.text = data[position].search.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
