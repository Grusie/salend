package cf.untitled.salend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemSearchStoreBinding
import cf.untitled.salend.model.StoreData
import com.bumptech.glide.Glide

class SearchStoreAdapter(private var data: ArrayList<StoreData>) :
    RecyclerView.Adapter<SearchStoreAdapter.SearchStoreViewHolder>() {
    class SearchStoreViewHolder(binding: ItemSearchStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var holderLeft = binding.storeLeftItemView
        var holderRight = binding.storeRightItemView
        var storeName = binding.itemSearchStoreName
        var storeImg = binding.itemSearchStoreImage
        var rightStoreName = binding.rightItemSearchStoreName
        var rightStoreImg = binding.rightItemSearchStoreImage
        var storeAddress = binding.itemSearchStoreAddress
        var rightStoreAddress = binding.rightItemSearchStoreAddress

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchStoreViewHolder {
        val view =
            ItemSearchStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchStoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchStoreViewHolder, position: Int) {
        val evenPosition = position * 2
        val oddPosition = position * 2 + 1
        holder.apply {
            if (evenPosition <= data.size - 1) {
                Glide.with(storeImg.context)
                    .load(data[evenPosition].s_image)
                    .error(R.drawable.ic_map_svgrepo_com)
                    .into(storeImg)

                storeName.text = data[evenPosition].s_name
                if (data[evenPosition].s_address != null) {
                    val list = data[evenPosition].s_address!!.split(" ")
                    storeAddress.text = "${list[0] + " " + list[1] + " " + list[2]}"
                }

                holderLeft.setOnClickListener {
                    val intent = Intent(itemView.context, StoreChoiceActivity::class.java)
                    data[evenPosition].apply {
                        intent.putExtra("category", "")
                        intent.putExtra("name", s_name)
                        intent.putExtra("time", s_time)
                        intent.putExtra("image", s_image)
                        intent.putExtra("id", _id)
                        ContextCompat.startActivity(holder.itemView.context, intent, null)
                    }
                }
            }
            if (data.size % 2 == 1 && evenPosition == data.size - 1) {
                holderRight.visibility = View.INVISIBLE
            }
            if (oddPosition <= data.size - 1) {
                Glide.with(rightStoreImg.context)
                    .load(data[oddPosition].s_image)
                    .error(R.drawable.ic_map_svgrepo_com)
                    .into(rightStoreImg)

                rightStoreName.text = data[oddPosition].s_name
                if (data[oddPosition].s_address != null) {
                    val list = data[oddPosition].s_address!!.split(" ")
                    rightStoreAddress.text = "${list[0] + " " + list[1] + " " + list[2]}"
                }
                holderRight.setOnClickListener {
                    val intent = Intent(itemView.context, StoreChoiceActivity::class.java)
                    data[oddPosition].apply {
                        intent.putExtra("category", "")
                        intent.putExtra("name", s_name)
                        intent.putExtra("time", s_time)
                        intent.putExtra("image", s_image)
                        intent.putExtra("id", _id)
                        ContextCompat.startActivity(holder.itemView.context, intent, null)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (data.size % 2 == 0)
            return data.size / 2
        else
            return (data.size / 2) + 1
    }
}