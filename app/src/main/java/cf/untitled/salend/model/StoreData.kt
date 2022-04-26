package cf.untitled.salend.model

data class StoreData(
    var s_id: String,
    var s_name: String?,
    var s_location: String?,
    var s_time: String?,
    var s_image: String?,
    var s_tag: List<Int>?
)
