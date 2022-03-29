package cf.untitled.salend.adapter

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemNearbySaleBinding
import cf.untitled.salend.model.productInfo

class NearbySaleRecyclerAdapter(private var datas: Array<productInfo>) :
    RecyclerView.Adapter<NearbySaleRecyclerAdapter.NearbySaleViewHolder>() {
    class NearbySaleViewHolder(binding: ItemNearbySaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val holder2 = binding.rightItemView
        val img = binding.productImg
        val shop = binding.shopName
        val name = binding.productName
        val price = binding.productPrice

        val img2 = binding.productImg2
        val shop2 = binding.shopName2
        val name2 = binding.productName2
        val price2 = binding.productPrice2
    }

    override fun onBindViewHolder(holder: NearbySaleViewHolder, position: Int) {

        holder.apply {
            Log.d("grusie", "$position")
            val evenPosition = position * 2
            val oddPosition = position * 2 + 1
            img.setImageResource(datas[evenPosition].img)
            shop.text = datas[evenPosition].shop
            name.text = datas[evenPosition].name
            price.text = datas[evenPosition].price.toString()

            try{
                img2.setImageResource(datas[oddPosition].img)
                shop2.text = datas[oddPosition].shop
                name2.text = datas[oddPosition].name
                price2.text = datas[oddPosition].price.toString()
            }catch (e:Exception) {
                holder2.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return Math.round((datas.size / 2).toDouble()).toInt() + 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NearbySaleRecyclerAdapter.NearbySaleViewHolder {
        return NearbySaleRecyclerAdapter.NearbySaleViewHolder(
            ItemNearbySaleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}