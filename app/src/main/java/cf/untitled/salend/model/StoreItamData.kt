package cf.untitled.salend.model


import com.google.gson.annotations.SerializedName

data class StoreItamData(
    @SerializedName("items")
    val items: List<StoreItem>?
)