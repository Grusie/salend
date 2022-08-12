package cf.untitled.salend.model

data class StoreData(
    var _id: String,
    var s_name: String?,
    var s_address: String?,
    var s_time: String?,
    var s_image: String?,
    var s_certified:Boolean?,
    var s_tag: List<Int>?,
    var s_lat: Double?,
    var s_lng: Double?,
)
