package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemNearbySaleBinding
import cf.untitled.salend.model.productInfo

class NearbySaleRecyclerAdapter(private var datas: Array<productInfo>) : RecyclerView.Adapter<NearbySaleRecyclerAdapter.NearbySaleViewHolder>() {
    class NearbySaleViewHolder(binding: ItemNearbySaleBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.productImg
        val shop = binding.shopName
        val name = binding.productName
        val price = binding.productPrice
    }

    override fun onBindViewHolder(holder: NearbySaleViewHolder, position: Int) {
        holder.apply{
            img.setImageResource(datas[position].img)
            shop.text = datas[position].shop
            name.text = datas[position].name
            price.text = datas[position].price.toString()
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbySaleViewHolder {
        return NearbySaleViewHolder(ItemNearbySaleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}