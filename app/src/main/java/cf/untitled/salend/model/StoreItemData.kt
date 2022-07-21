package cf.untitled.salend.model


import com.google.gson.annotations.SerializedName

data class StoreItemData(
    @SerializedName("items")
    val items: List<StoreItem>?
)