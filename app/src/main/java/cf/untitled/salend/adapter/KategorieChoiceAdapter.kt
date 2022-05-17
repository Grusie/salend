package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.model.KategoriStoreData

class KategorieChoiceAdapter(val kategorieList : ArrayList<KategoriStoreData>) :
    RecyclerView.Adapter<KategorieChoiceAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategorieChoiceAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kategorie,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategorieChoiceAdapter.CustomViewHolder, position: Int) {
        holder.storeImage.setImageResource(kategorieList.get(position).storeImage)
        holder.storeName.setText(kategorieList.get(position).storeName)

    }

    override fun getItemCount(): Int {
        return kategorieList.size
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val storeImage = itemView.findViewById<ImageView>(R.id.kategorie_store_image)
        val storeName = itemView.findViewById<TextView>(R.id.kategorie_store_name)
    }
}
