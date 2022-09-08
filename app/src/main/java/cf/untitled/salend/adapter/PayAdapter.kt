package cf.untitled.salend.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.databinding.ItemPayBinding
import cf.untitled.salend.model.BuyData
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.text.SimpleDateFormat

//결제 리스트를 띄우기 위한 리사이클러뷰 구조 구현
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
        val buyInfo = data[position]
        val itemInfo = JSONObject(buyInfo.b_item)
        holder.apply {
            price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            productName.text = itemInfo.getString("i_name")
            storeName.text = itemInfo.getString("i_store_name")
            price.text = itemInfo.getString("i_price") + "￦"
            nowPrice.text = itemInfo.getString("i_now_price") + "￦"

            val pattern = "yyyy.MM.dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            date.text = simpleDateFormat.format(buyInfo.b_date)
            Glide.with(img.context).load(itemInfo.getString("i_image"))
                .error(R.drawable.ic_shopping_selected).into(img)
        }
    }
}