package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.databinding.ItemPayBinding
import cf.untitled.salend.model.BuyData
import com.bumptech.glide.Glide

class PayAdapter(private var data: ArrayList<BuyData>) :
    RecyclerView.Adapter<PayAdapter.PayViewHolder>() {
    class PayViewHolder(binding: ItemPayBinding) : RecyclerView.ViewHolder(binding.root) {

        var productName = binding.payProductName
        var storeName = binding.payStoreName
        var price = binding.payPrice
        var nowPrice = binding.payNowPrice
        var date = binding.payDate
        var img = binding.payProductImg
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayViewHolder {
        return PayViewHolder(
            ItemPayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PayViewHolder, position: Int) {
        var buyInfo = data[position]
        holder.apply {
            productName.text = buyInfo.b_item?.i_name
            storeName.text = buyInfo.b_item?.i_store_name
            price.text = buyInfo.b_item?.i_price.toString()
            nowPrice.text = buyInfo.b_item?.i_now_price.toString()
            date.text = buyInfo.b_date.toString()
            Glide.with(img.context).load(buyInfo.b_item?.i_image)
                .error(R.drawable.ic_shopping_selected).into(img)
        }
    }
}