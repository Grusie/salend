package cf.untitled.salend.model


import com.google.gson.annotations.SerializedName

data class StoreItem(
    @SerializedName("i_image")
    val iImage: String?,
    @SerializedName("i_name")
    val iName: String?,
    @SerializedName("i_now_price")
    val iNowPrice: Int?,
    @SerializedName("i_price")
    val iPrice: Int?,
    @SerializedName("i_status")
    val iStatus: Int?,
    @SerializedName("i_store_name")
    val iStoreName: String?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("__v")
    val v: Int?
)